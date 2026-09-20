package io.github.danielbul464.androidcodingrecipes.exo_player.core.cache

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.source.MediaSource
import io.github.danielbul464.androidcodingrecipes.exo_player.core.VideoPlayerBuilder

/**
 * Cached media source configuration for [VideoPlayerBuilder].
 * The implementation defines data storage, the network source, cache keys, and the eviction policy.
 * The player only uses the provided [MediaSource.Factory] and does not manage the shared cache lifecycle.
 */
@OptIn(UnstableApi::class)
abstract class VideoPlayerCache {

    /** @return the media source factory configured by the [VideoPlayerCache] implementation. */
    protected abstract fun createMediaSourceFactory(): MediaSource.Factory

    internal fun mediaSourceFactory(): MediaSource.Factory = createMediaSourceFactory()
}
