package io.github.danielbul464.androidcodingrecipes.exo_player.core.domain

/**
 * A listener for observing [VideoPlayer] state changes and errors.
 */
interface VideoPlayerListener {

    /** Called when the playback state changes. */
    fun onPlaybackStateChanged(state: VideoPlaybackState) = Unit

    /** Called when the first frame has been rendered. */
    fun onRenderedFirstFrame() = Unit

    /** Called when the player starts or stops actively playing. */
    fun onIsPlayingChanged(isPlaying: Boolean) = Unit

    /** Called when the player starts or stops loading data. */
    fun onIsLoadingChanged(isLoading: Boolean) = Unit

    /** Called when the intention to start playback once the player is ready changes. */
    fun onPlayWhenReadyChanged(
        playWhenReady: Boolean,
        reason: VideoPlayerPlayWhenReadyChangeReason,
    ) = Unit

    /** Called when playback transitions to another queue item or repeats the current one. */
    fun onMediaItemTransition(
        mediaItemIndex: Int?,
        reason: VideoPlayerMediaItemTransitionReason,
    ) = Unit

    /** Called when the playback position changes discontinuously due to seeking or an item transition. */
    fun onPositionDiscontinuity(
        oldPosition: VideoPlayerPositionInfo,
        newPosition: VideoPlayerPositionInfo,
        reason: VideoPlayerPositionDiscontinuityReason,
    ) = Unit

    /** Called when a player error occurs. */
    fun onPlayerError(error: VideoPlayerError) = Unit
}
