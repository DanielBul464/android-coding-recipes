package io.github.danielbul464.androidcodingrecipes.exo_player.core

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SeekParameters
import io.github.danielbul464.androidcodingrecipes.exo_player.core.cache.VideoPlayerCache
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayer
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerRepeatMode
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerSeekMode
import io.github.danielbul464.androidcodingrecipes.exo_player.core.performance.VideoPlaybackAnalyticsConfig
import io.github.danielbul464.androidcodingrecipes.exo_player.core.performance.createPlaybackStatsListener

@OptIn(UnstableApi::class)
class VideoPlayerBuilder(private val context: Context) {

    private var playWhenReady: Boolean = true
    private var repeatMode: VideoPlayerRepeatMode = VideoPlayerRepeatMode.Off
    private var playerName: String? = null
    private var handleAudioFocus: Boolean? = null
    private var handleAudioBecomingNoisy: Boolean? = null
    private var pauseAtEndOfMediaItems: Boolean? = null
    private var seekBackIncrementMs: Long? = null
    private var seekForwardIncrementMs: Long? = null
    private var maxSeekToPreviousPositionMs: Long? = null
    private var seekMode: VideoPlayerSeekMode? = null
    private var useLazyPreparation: Boolean? = null
    private var enableDecoderFallback: Boolean? = null
    private var playlistPreloadDurationMs: Long? = null
    private var bufferDurations: BufferDurations? = null
    private var prioritizeTimeOverSizeThresholds: Boolean? = null
    private var backBuffer: BackBuffer? = null
    private var volume: Float? = null
    private var cache: VideoPlayerCache? = null
    private var playbackAnalyticsConfig: VideoPlaybackAnalyticsConfig =
        VideoPlaybackAnalyticsConfig.Disabled

    fun setPlayWhenReady(playWhenReady: Boolean) = apply {
        this.playWhenReady = playWhenReady
    }

    fun setRepeatMode(repeatMode: VideoPlayerRepeatMode) = apply {
        this.repeatMode = repeatMode
    }

    fun setName(name: String) = apply {
        require(name.isNotBlank()) { "Player name must not be blank" }
        playerName = name
    }

    fun setHandleAudioFocus(handleAudioFocus: Boolean) = apply {
        this.handleAudioFocus = handleAudioFocus
    }

    fun setHandleAudioBecomingNoisy(handleAudioBecomingNoisy: Boolean) = apply {
        this.handleAudioBecomingNoisy = handleAudioBecomingNoisy
    }

    fun setPauseAtEndOfMediaItems(pauseAtEndOfMediaItems: Boolean) = apply {
        this.pauseAtEndOfMediaItems = pauseAtEndOfMediaItems
    }

    fun setSeekBackIncrementMs(seekBackIncrementMs: Long) = apply {
        this.seekBackIncrementMs = seekBackIncrementMs
    }

    fun setSeekForwardIncrementMs(seekForwardIncrementMs: Long) = apply {
        this.seekForwardIncrementMs = seekForwardIncrementMs
    }

    fun setMaxSeekToPreviousPositionMs(maxSeekToPreviousPositionMs: Long) = apply {
        this.maxSeekToPreviousPositionMs = maxSeekToPreviousPositionMs
    }

    fun setSeekMode(seekMode: VideoPlayerSeekMode) = apply {
        this.seekMode = seekMode
    }

    fun setUseLazyPreparation(useLazyPreparation: Boolean) = apply {
        this.useLazyPreparation = useLazyPreparation
    }

    fun setEnableDecoderFallback(enableDecoderFallback: Boolean) = apply {
        this.enableDecoderFallback = enableDecoderFallback
    }

    fun setPlaylistPreloadDurationMs(playlistPreloadDurationMs: Long) = apply {
        require(playlistPreloadDurationMs > 0) { "Playlist preload duration must be positive" }
        this.playlistPreloadDurationMs = playlistPreloadDurationMs
    }

    fun setBufferDurationsMs(
        minBufferMs: Int,
        maxBufferMs: Int,
        bufferForPlaybackMs: Int,
        bufferForPlaybackAfterRebufferMs: Int,
    ) = apply {
        bufferDurations =
            BufferDurations(
                minBufferMs = minBufferMs,
                maxBufferMs = maxBufferMs,
                bufferForPlaybackMs = bufferForPlaybackMs,
                bufferForPlaybackAfterRebufferMs = bufferForPlaybackAfterRebufferMs,
            )
    }

    fun setPrioritizeTimeOverSizeThresholds(prioritizeTimeOverSizeThresholds: Boolean) = apply {
        this.prioritizeTimeOverSizeThresholds = prioritizeTimeOverSizeThresholds
    }

    fun setBackBuffer(
        backBufferDurationMs: Int,
        retainBackBufferFromKeyframe: Boolean,
    ) = apply {
        backBuffer =
            BackBuffer(
                durationMs = backBufferDurationMs,
                retainFromKeyframe = retainBackBufferFromKeyframe,
            )
    }

    fun setVolume(volume: Float) = apply {
        this.volume = volume.coerceIn(0f, 1f)
    }

    fun setCache(cache: VideoPlayerCache) = apply {
        this.cache = cache
    }

    fun setPlaybackAnalyticsEnabled(
        enabled: Boolean,
        tag: String = VideoPlaybackAnalyticsConfig.DEFAULT_TAG,
    ) = apply {
        playbackAnalyticsConfig =
            VideoPlaybackAnalyticsConfig(
                isEnabled = enabled,
                tag = tag,
            )
    }

    fun build(): VideoPlayer {
        val player =
            ExoPlayer.Builder(context.applicationContext)
                .apply {
                    this@VideoPlayerBuilder.cache
                        ?.mediaSourceFactory()
                        ?.let(::setMediaSourceFactory)
                    this@VideoPlayerBuilder.playerName?.let(::setName)
                    this@VideoPlayerBuilder.handleAudioFocus?.let {
                        setAudioAttributes(AudioAttributes.DEFAULT, it)
                    }
                    this@VideoPlayerBuilder.handleAudioBecomingNoisy?.let(::setHandleAudioBecomingNoisy)
                    this@VideoPlayerBuilder.pauseAtEndOfMediaItems?.let(::setPauseAtEndOfMediaItems)
                    this@VideoPlayerBuilder.seekBackIncrementMs?.let(::setSeekBackIncrementMs)
                    this@VideoPlayerBuilder.seekForwardIncrementMs?.let(::setSeekForwardIncrementMs)
                    this@VideoPlayerBuilder.maxSeekToPreviousPositionMs?.let(
                        ::setMaxSeekToPreviousPositionMs,
                    )
                    this@VideoPlayerBuilder.seekMode?.toSeekParameters()?.let(::setSeekParameters)
                    this@VideoPlayerBuilder.useLazyPreparation?.let(::setUseLazyPreparation)
                    this@VideoPlayerBuilder.createRenderersFactory()?.let(::setRenderersFactory)
                    this@VideoPlayerBuilder.createLoadControl()?.let(::setLoadControl)
                }
                .build()
                .apply {
                    playWhenReady = this@VideoPlayerBuilder.playWhenReady
                    repeatMode = this@VideoPlayerBuilder.repeatMode.platformValue
                    this@VideoPlayerBuilder.volume?.let { volume = it }
                    this@VideoPlayerBuilder.playlistPreloadDurationMs?.let {
                        preloadConfiguration = ExoPlayer.PreloadConfiguration(Util.msToUs(it))
                    }

                    if (playbackAnalyticsConfig.isEnabled) {
                        addAnalyticsListener(createPlaybackStatsListener(playbackAnalyticsConfig))
                    }
                }

        return ExoVideoPlayer(
            player = player,
        )
    }

    private fun createRenderersFactory(): DefaultRenderersFactory? =
        enableDecoderFallback?.let { enableDecoderFallback ->
            DefaultRenderersFactory(context.applicationContext).apply {
                setEnableDecoderFallback(enableDecoderFallback)
            }
        }

    private fun createLoadControl(): DefaultLoadControl? {
        if (
            bufferDurations == null &&
            prioritizeTimeOverSizeThresholds == null &&
            backBuffer == null
        ) {
            return null
        }

        return DefaultLoadControl.Builder()
            .apply {
                this@VideoPlayerBuilder.bufferDurations?.let {
                    setBufferDurationsMs(
                        it.minBufferMs,
                        it.maxBufferMs,
                        it.bufferForPlaybackMs,
                        it.bufferForPlaybackAfterRebufferMs,
                    )
                }
                this@VideoPlayerBuilder.prioritizeTimeOverSizeThresholds?.let(
                    ::setPrioritizeTimeOverSizeThresholds,
                )
                this@VideoPlayerBuilder.backBuffer?.let {
                    setBackBuffer(it.durationMs, it.retainFromKeyframe)
                }
            }
            .build()
    }
}

private data class BufferDurations(
    val minBufferMs: Int,
    val maxBufferMs: Int,
    val bufferForPlaybackMs: Int,
    val bufferForPlaybackAfterRebufferMs: Int,
)

private data class BackBuffer(
    val durationMs: Int,
    val retainFromKeyframe: Boolean,
)

@OptIn(UnstableApi::class)
private fun VideoPlayerSeekMode.toSeekParameters(): SeekParameters =
    when (this) {
        VideoPlayerSeekMode.Exact -> SeekParameters.EXACT
        VideoPlayerSeekMode.PreviousSync -> SeekParameters.PREVIOUS_SYNC
        VideoPlayerSeekMode.NextSync -> SeekParameters.NEXT_SYNC
        VideoPlayerSeekMode.ClosestSync -> SeekParameters.CLOSEST_SYNC
    }
