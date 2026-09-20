package io.github.danielbul464.androidcodingrecipes.exo_player.cache_recipe

import android.content.Context
import android.content.pm.ApplicationInfo
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheKeyFactory
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import io.github.danielbul464.androidcodingrecipes.exo_player.core.cache.VideoPlayerCache
import io.github.danielbul464.androidcodingrecipes.exo_player.core.cache.VideoPlayerCacheConfig
import io.github.danielbul464.androidcodingrecipes.exo_player.core.cache.VideoPlayerCacheManager
import io.github.danielbul464.androidcodingrecipes.exo_player.core.cache.VideoPlayerCacheSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Application-specific implementation of a shared disk cache for video players.
 *
 * [SimpleCache] stores downloaded media ranges in a dedicated application directory and restores
 * them on the next launch. [LeastRecentlyUsedCacheEvictor] limits disk usage by removing the least
 * recently used ranges when the cache reaches its configured size. [StandaloneDatabaseProvider]
 * stores the Media3 index in SQLite to associate cache keys and metadata with physical files after
 * the process restarts.
 *
 * [CacheDataSource] reads available ranges from [SimpleCache], fetches missing ranges from the
 * upstream [DefaultDataSource], and writes them to the cache. Create one instance for the application
 * and share it across players: Media3 allows only one [SimpleCache] instance per directory within
 * a process. Releasing an individual player does not release the shared cache.
 *
 * Official documentation:
 * - [Caching media in Media3](https://developer.android.com/media/media3/exoplayer/network-stacks#caching-media)
 * - [SimpleCache](https://developer.android.com/reference/androidx/media3/datasource/cache/SimpleCache)
 * - [LeastRecentlyUsedCacheEvictor](https://developer.android.com/reference/androidx/media3/datasource/cache/LeastRecentlyUsedCacheEvictor)
 * - [StandaloneDatabaseProvider](https://developer.android.com/reference/androidx/media3/database/StandaloneDatabaseProvider)
 * - [CacheDataSource](https://developer.android.com/reference/androidx/media3/datasource/cache/CacheDataSource)
 *
 * @param context a context whose application context is used to access the cache directory and
 *   create Media3 Android components.
 * @param config cache directory, size limit, error handling, and diagnostic logging settings.
 *   Defaults to a 64 MiB cache in `video_player_cache`, with cache errors ignored and logging disabled.
 */
@OptIn(UnstableApi::class)
class VideoPlayerCacheImpl(
    context: Context,
    private val config: VideoPlayerCacheConfig = VideoPlayerCacheConfig(
        directoryName = VIDEO_PLAYER_CACHE_DIRECTORY_NAME,
        maxSizeBytes = VIDEO_PLAYER_CACHE_MAX_SIZE_BYTES,
        ignoreCacheOnError = VIDEO_PLAYER_IGNORE_CACHE_ON_ERROR,
        isLoggingEnabled = VIDEO_PLAYER_CACHE_LOGGING_ENABLED,
    ),
) : VideoPlayerCache(), VideoPlayerCacheProvider, VideoPlayerCacheManager {

    private val applicationContext = context.applicationContext

    /**
     * Serializes administrative cache operations.
     *
     * [SimpleCache] synchronizes individual API calls, but [clear] retrieves keys and performs a
     * series of removals. Without this lock, [snapshot] could read an intermediate state during
     * clearing. This lock intentionally does not block reads and writes from [CacheDataSource].
     */
    private val cacheOperationsMutex = Mutex()

    /**
     * The physical storage for downloaded ranges and their index.
     *
     * Initialized lazily to avoid opening the directory and database until a player or an
     * administrative operation needs the cache. Synchronized initialization ensures that concurrent
     * access from multiple threads creates only one instance.
     */
    private val simpleCache: SimpleCache by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        SimpleCache(
            File(applicationContext.cacheDir, config.directoryName),
            LeastRecentlyUsedCacheEvictor(config.maxSizeBytes),
            StandaloneDatabaseProvider(applicationContext),
        )
    }

    /**
     * The shared media source factory that routes reads through the configured [CacheDataSource].
     *
     * Created once and reused by all players connected to this cache.
     */
    private val mediaSourceFactory: MediaSource.Factory by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        DefaultMediaSourceFactory(applicationContext)
            .setDataSourceFactory(createCacheDataSourceFactory())
    }

    /** Exposes the cache to application code without revealing Media3 or file system details. */
    override val cache: VideoPlayerCache = this

    /** @return the shared media source factory configured to read and write through the cache. */
    override fun createMediaSourceFactory(): MediaSource.Factory = mediaSourceFactory

    /** Removes all resources from the active cache, preserving its directory and configuration. */
    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            cacheOperationsMutex.withLock {
                simpleCache.keys.toList().forEach(simpleCache::removeResource)
            }
        }
    }

    /** @return the current size, size limit, and number of fully or partially cached resources. */
    override suspend fun snapshot(): VideoPlayerCacheSnapshot =
        withContext(Dispatchers.IO) {
            cacheOperationsMutex.withLock {
                VideoPlayerCacheSnapshot(
                    usedSizeBytes = simpleCache.cacheSpace,
                    maxSizeBytes = config.maxSizeBytes,
                    cachedResourcesCount = simpleCache.keys.size,
                )
            }
        }

    /**
     * @return a data source factory that reads available ranges from the cache and, on a cache miss,
     *   fetches data from the upstream [DefaultDataSource] and stores it in [SimpleCache].
     */
    private fun createCacheDataSourceFactory(): CacheDataSource.Factory =
        CacheDataSource.Factory()
            .setCache(simpleCache)
            .setCacheKeyFactory(CacheKeyFactory.DEFAULT)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(applicationContext))
            .setFlags(
                if (config.ignoreCacheOnError) {
                    CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
                } else {
                    0
                }
            )
            .apply {
                if (
                    config.isLoggingEnabled &&
                    applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
                ) {
                    setEventListener(createCacheEventListener())
                }
            }

    /** @return a listener for diagnosing cache reads in debuggable applications. */
    private fun createCacheEventListener(): CacheDataSource.EventListener =
        object : CacheDataSource.EventListener {

            override fun onCachedBytesRead(cacheSizeBytes: Long, cachedBytesRead: Long) {
                Log.d(
                    VIDEO_PLAYER_CACHE_DEBUG_LOG_TAG,
                    "cacheHit: cachedBytesRead=$cachedBytesRead, cacheSizeBytes=$cacheSizeBytes",
                )
            }

            override fun onCacheIgnored(reason: Int) {
                Log.d(VIDEO_PLAYER_CACHE_DEBUG_LOG_TAG, "cacheIgnored: reason=$reason")
            }
        }

    private companion object {
        const val VIDEO_PLAYER_CACHE_DIRECTORY_NAME = "video_player_cache"
        const val VIDEO_PLAYER_CACHE_MAX_SIZE_BYTES = 64L * 1024 * 1024
        const val VIDEO_PLAYER_CACHE_LOGGING_ENABLED = false
        const val VIDEO_PLAYER_IGNORE_CACHE_ON_ERROR = true
        const val VIDEO_PLAYER_CACHE_DEBUG_LOG_TAG = "VideoPlayerCacheDebug"
    }
}
