package io.github.danielbul464.androidcodingrecipes.exo_player.core

import android.os.Looper
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.PlayerMessage
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlaybackState
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayer
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerError
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerListener
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerMediaItemTransitionReason
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerPlayWhenReadyChangeReason
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerPositionDiscontinuityReason
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerPositionInfo
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerRepeatMode
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerScheduledAction
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerSource
import kotlin.time.Duration

@OptIn(UnstableApi::class)
internal class ExoVideoPlayer(
    private val player: ExoPlayer,
) : VideoPlayer {

    private val listeners = mutableMapOf<VideoPlayerListener, Player.Listener>()
    private val scheduledActions = mutableSetOf<ExoVideoPlayerScheduledAction>()

    override val durationMs: Long
        get() = player.duration

    override val currentPositionMs: Long
        get() = player.currentPosition

    override val bufferedPositionMs: Long
        get() = player.bufferedPosition

    override val totalBufferedDurationMs: Long
        get() = player.totalBufferedDuration

    override val bufferedPercentage: Int
        get() = player.bufferedPercentage

    override val playbackState: VideoPlaybackState
        get() = player.playbackState.toVideoPlaybackState()

    override val playerError: VideoPlayerError?
        get() = player.playerError?.toVideoPlayerError()

    override val isPlaying: Boolean
        get() = player.isPlaying

    override val isLoading: Boolean
        get() = player.isLoading

    override val playWhenReady: Boolean
        get() = player.playWhenReady

    override val playbackSpeed: Float
        get() = player.playbackParameters.speed

    override val volume: Float
        get() = player.volume

    override val repeatMode: VideoPlayerRepeatMode
        get() = player.repeatMode.toVideoPlayerRepeatMode()

    override val shuffleModeEnabled: Boolean
        get() = player.shuffleModeEnabled

    override val pauseAtEndOfMediaItems: Boolean
        get() = player.pauseAtEndOfMediaItems

    override val mediaItemCount: Int
        get() = player.mediaItemCount

    override val currentMediaItemIndex: Int
        get() = player.currentMediaItemIndex

    override val nextMediaItemIndex: Int
        get() = player.nextMediaItemIndex

    override val previousMediaItemIndex: Int
        get() = player.previousMediaItemIndex

    override val hasNextMediaItem: Boolean
        get() = player.hasNextMediaItem()

    override val hasPreviousMediaItem: Boolean
        get() = player.hasPreviousMediaItem()

    override val seekBackIncrementMs: Long
        get() = player.seekBackIncrement

    override val seekForwardIncrementMs: Long
        get() = player.seekForwardIncrement

    override val maxSeekToPreviousPositionMs: Long
        get() = player.maxSeekToPreviousPosition

    override val isCurrentMediaItemSeekable: Boolean
        get() = player.isCurrentMediaItemSeekable

    override val isCurrentMediaItemLive: Boolean
        get() = player.isCurrentMediaItemLive

    override val isCurrentMediaItemDynamic: Boolean
        get() = player.isCurrentMediaItemDynamic

    override fun setSource(source: VideoPlayerSource) {
        player.setMediaItem(source.toMediaItem())
    }

    override fun setSource(source: VideoPlayerSource, startPositionMs: Long) {
        player.setMediaItem(source.toMediaItem(), startPositionMs)
    }

    override fun setSource(source: VideoPlayerSource, resetPosition: Boolean) {
        player.setMediaItem(source.toMediaItem(), resetPosition)
    }

    override fun setSources(sources: List<VideoPlayerSource>) {
        player.setMediaItems(sources.toMediaItems())
    }

    override fun setSources(sources: List<VideoPlayerSource>, resetPosition: Boolean) {
        player.setMediaItems(sources.toMediaItems(), resetPosition)
    }

    override fun setSources(
        sources: List<VideoPlayerSource>,
        startMediaItemIndex: Int,
        startPositionMs: Long,
    ) {
        player.setMediaItems(sources.toMediaItems(), startMediaItemIndex, startPositionMs)
    }

    override fun addSource(source: VideoPlayerSource) {
        player.addMediaItem(source.toMediaItem())
    }

    override fun addSource(index: Int, source: VideoPlayerSource) {
        player.addMediaItem(index, source.toMediaItem())
    }

    override fun addSources(sources: List<VideoPlayerSource>) {
        player.addMediaItems(sources.toMediaItems())
    }

    override fun addSources(index: Int, sources: List<VideoPlayerSource>) {
        player.addMediaItems(index, sources.toMediaItems())
    }

    override fun moveSource(currentIndex: Int, newIndex: Int) {
        player.moveMediaItem(currentIndex, newIndex)
    }

    override fun moveSources(fromIndex: Int, toIndex: Int, newIndex: Int) {
        player.moveMediaItems(fromIndex, toIndex, newIndex)
    }

    override fun replaceSource(index: Int, source: VideoPlayerSource) {
        player.replaceMediaItem(index, source.toMediaItem())
    }

    override fun replaceSources(
        fromIndex: Int,
        toIndex: Int,
        sources: List<VideoPlayerSource>,
    ) {
        player.replaceMediaItems(fromIndex, toIndex, sources.toMediaItems())
    }

    override fun removeSource(index: Int) {
        player.removeMediaItem(index)
    }

    override fun removeSources(fromIndex: Int, toIndex: Int) {
        player.removeMediaItems(fromIndex, toIndex)
    }

    override fun prepare() {
        player.prepare()
    }

    override fun play() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun setPlayWhenReady(playWhenReady: Boolean) {
        player.playWhenReady = playWhenReady
    }

    override fun stop() {
        player.stop()
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    override fun seekTo(mediaItemIndex: Int, positionMs: Long) {
        player.seekTo(mediaItemIndex, positionMs)
    }

    override fun seekToDefaultPosition() {
        player.seekToDefaultPosition()
    }

    override fun seekToDefaultPosition(mediaItemIndex: Int) {
        player.seekToDefaultPosition(mediaItemIndex)
    }

    override fun seekBack() {
        player.seekBack()
    }

    override fun seekForward() {
        player.seekForward()
    }

    override fun seekToPreviousMediaItem() {
        player.seekToPreviousMediaItem()
    }

    override fun seekToNextMediaItem() {
        player.seekToNextMediaItem()
    }

    override fun seekToPrevious() {
        player.seekToPrevious()
    }

    override fun seekToNext() {
        player.seekToNext()
    }

    override fun setRepeatMode(repeatMode: VideoPlayerRepeatMode) {
        player.repeatMode = repeatMode.platformValue
    }

    override fun setShuffleModeEnabled(shuffleModeEnabled: Boolean) {
        player.shuffleModeEnabled = shuffleModeEnabled
    }

    override fun setPauseAtEndOfMediaItems(pauseAtEndOfMediaItems: Boolean) {
        player.pauseAtEndOfMediaItems = pauseAtEndOfMediaItems
    }

    override fun setPlaybackSpeed(speed: Float) {
        player.setPlaybackSpeed(speed)
    }

    override fun setVolume(volume: Float) {
        player.volume = volume
    }

    override fun mute() {
        player.mute()
    }

    override fun unmute() {
        player.unmute()
    }

    override fun clearSources() {
        player.clearMediaItems()
    }

    override fun addListener(listener: VideoPlayerListener) {
        if (listeners.containsKey(listener)) return

        val playerListener =
            object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    listener.onPlaybackStateChanged(playbackState.toVideoPlaybackState())
                }

                override fun onRenderedFirstFrame() {
                    listener.onRenderedFirstFrame()
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    listener.onIsPlayingChanged(isPlaying)
                }

                override fun onIsLoadingChanged(isLoading: Boolean) {
                    listener.onIsLoadingChanged(isLoading)
                }

                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    listener.onPlayWhenReadyChanged(
                        playWhenReady = playWhenReady,
                        reason = reason.toVideoPlayerPlayWhenReadyChangeReason(),
                    )
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    listener.onMediaItemTransition(
                        mediaItemIndex = mediaItem?.let { player.currentMediaItemIndex },
                        reason = reason.toVideoPlayerMediaItemTransitionReason(),
                    )
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int,
                ) {
                    listener.onPositionDiscontinuity(
                        oldPosition = oldPosition.toVideoPlayerPositionInfo(),
                        newPosition = newPosition.toVideoPlayerPositionInfo(),
                        reason = reason.toVideoPlayerPositionDiscontinuityReason(),
                    )
                }

                override fun onPlayerError(error: PlaybackException) {
                    listener.onPlayerError(error.toVideoPlayerError())
                }
            }

        listeners[listener] = playerListener
        player.addListener(playerListener)
    }

    override fun removeListener(listener: VideoPlayerListener) {
        val playerListener = listeners.remove(listener) ?: return
        player.removeListener(playerListener)
    }

    override fun scheduleActionAt(
        position: Duration,
        mediaItemIndex: Int?,
        deleteAfterDelivery: Boolean,
        action: () -> Unit,
    ): VideoPlayerScheduledAction {
        lateinit var scheduledAction: ExoVideoPlayerScheduledAction

        val message =
            player
                .createMessage { _, _ ->
                    if (deleteAfterDelivery) {
                        scheduledActions.remove(scheduledAction)
                    }
                    action()
                }
                .setLooper(Looper.getMainLooper())
                .setDeleteAfterDelivery(deleteAfterDelivery)

        if (mediaItemIndex == null) {
            message.setPosition(position.inWholeMilliseconds)
        } else {
            message.setPosition(mediaItemIndex, position.inWholeMilliseconds)
        }

        scheduledAction = ExoVideoPlayerScheduledAction(message) {
            scheduledActions.remove(it)
        }

        scheduledActions += scheduledAction
        message.send()

        return scheduledAction
    }

    override fun release() {
        scheduledActions.toList().forEach(VideoPlayerScheduledAction::cancel)
        listeners.values.forEach(player::removeListener)
        listeners.clear()
        player.release()
    }

    internal fun asPlatformPlayer(): Player = player
}

@OptIn(UnstableApi::class)
private class ExoVideoPlayerScheduledAction(
    private val message: PlayerMessage,
    private val onCancel: (ExoVideoPlayerScheduledAction) -> Unit,
) : VideoPlayerScheduledAction {

    override fun cancel() {
        message.cancel()
        onCancel(this)
    }
}

private fun VideoPlayerSource.toMediaItem(): MediaItem = MediaItem.fromUri(uri)

private fun List<VideoPlayerSource>.toMediaItems(): List<MediaItem> = map(VideoPlayerSource::toMediaItem)

private fun PlaybackException.toVideoPlayerError(): VideoPlayerError =
    VideoPlayerError(
        errorCode = errorCode,
        errorCodeName = errorCodeName,
        message = message,
        cause = cause,
    )

private fun Player.PositionInfo.toVideoPlayerPositionInfo(): VideoPlayerPositionInfo =
    VideoPlayerPositionInfo(
        mediaItemIndex = mediaItemIndex,
        positionMs = positionMs,
        contentPositionMs = contentPositionMs,
    )

private fun Int.toVideoPlayerPlayWhenReadyChangeReason(): VideoPlayerPlayWhenReadyChangeReason =
    when (this) {
        Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST ->
            VideoPlayerPlayWhenReadyChangeReason.UserRequest
        Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_FOCUS_LOSS ->
            VideoPlayerPlayWhenReadyChangeReason.AudioFocusLoss
        Player.PLAY_WHEN_READY_CHANGE_REASON_AUDIO_BECOMING_NOISY ->
            VideoPlayerPlayWhenReadyChangeReason.AudioBecomingNoisy
        Player.PLAY_WHEN_READY_CHANGE_REASON_REMOTE ->
            VideoPlayerPlayWhenReadyChangeReason.Remote
        Player.PLAY_WHEN_READY_CHANGE_REASON_END_OF_MEDIA_ITEM ->
            VideoPlayerPlayWhenReadyChangeReason.EndOfMediaItem
        Player.PLAY_WHEN_READY_CHANGE_REASON_SUPPRESSED_TOO_LONG ->
            VideoPlayerPlayWhenReadyChangeReason.SuppressedTooLong
        else -> VideoPlayerPlayWhenReadyChangeReason.Unknown
    }

private fun Int.toVideoPlayerMediaItemTransitionReason(): VideoPlayerMediaItemTransitionReason =
    when (this) {
        Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT -> VideoPlayerMediaItemTransitionReason.Repeat
        Player.MEDIA_ITEM_TRANSITION_REASON_AUTO -> VideoPlayerMediaItemTransitionReason.Auto
        Player.MEDIA_ITEM_TRANSITION_REASON_SEEK -> VideoPlayerMediaItemTransitionReason.Seek
        Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED ->
            VideoPlayerMediaItemTransitionReason.PlaylistChanged
        else -> VideoPlayerMediaItemTransitionReason.Unknown
    }

private fun Int.toVideoPlayerPositionDiscontinuityReason(): VideoPlayerPositionDiscontinuityReason =
    when (this) {
        Player.DISCONTINUITY_REASON_AUTO_TRANSITION ->
            VideoPlayerPositionDiscontinuityReason.AutoTransition
        Player.DISCONTINUITY_REASON_SEEK -> VideoPlayerPositionDiscontinuityReason.Seek
        Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT ->
            VideoPlayerPositionDiscontinuityReason.SeekAdjustment
        Player.DISCONTINUITY_REASON_SKIP -> VideoPlayerPositionDiscontinuityReason.Skip
        Player.DISCONTINUITY_REASON_REMOVE -> VideoPlayerPositionDiscontinuityReason.Remove
        Player.DISCONTINUITY_REASON_INTERNAL -> VideoPlayerPositionDiscontinuityReason.Internal
        Player.DISCONTINUITY_REASON_SILENCE_SKIP ->
            VideoPlayerPositionDiscontinuityReason.SilenceSkip
        else -> VideoPlayerPositionDiscontinuityReason.Unknown
    }

private fun Int.toVideoPlaybackState(): VideoPlaybackState =
    when (this) {
        Player.STATE_IDLE -> VideoPlaybackState.Idle
        Player.STATE_BUFFERING -> VideoPlaybackState.Buffering
        Player.STATE_READY -> VideoPlaybackState.Ready
        Player.STATE_ENDED -> VideoPlaybackState.Ended
        else -> VideoPlaybackState.Idle
    }

private fun Int.toVideoPlayerRepeatMode(): VideoPlayerRepeatMode =
    when (this) {
        Player.REPEAT_MODE_ONE -> VideoPlayerRepeatMode.One
        Player.REPEAT_MODE_ALL -> VideoPlayerRepeatMode.All
        else -> VideoPlayerRepeatMode.Off
    }
