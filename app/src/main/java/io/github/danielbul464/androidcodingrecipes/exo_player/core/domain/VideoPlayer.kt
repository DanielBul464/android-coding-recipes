package io.github.danielbul464.androidcodingrecipes.exo_player.core.domain

import kotlin.time.Duration

/**
 * A video player abstraction.
 *
 * Instances are created through a builder and owned by an external component
 * such as a `ViewModel`, while the UI only controls playback.
 */
interface VideoPlayer {

    /** The total duration of the current video in milliseconds, if already known to the player. */
    val durationMs: Long

    /** The current playback position in milliseconds. */
    val currentPositionMs: Long

    /** The position up to which content has been buffered, in milliseconds. */
    val bufferedPositionMs: Long

    /** The total duration of buffered content from the current position, in milliseconds. */
    val totalBufferedDurationMs: Long

    /** The percentage of the current media item that has been buffered. */
    val bufferedPercentage: Int

    /** The current playback state. */
    val playbackState: VideoPlaybackState

    /** The current player error, or `null` if there is no error. */
    val playerError: VideoPlayerError?

    /** `true` if the player is currently playing content. */
    val isPlaying: Boolean

    /** `true` if the player is currently loading data. */
    val isLoading: Boolean

    /** Whether playback will start automatically when the player enters the Ready state. */
    val playWhenReady: Boolean

    /** The current playback speed. */
    val playbackSpeed: Float

    /** The current player volume, ranging from `0f` to `1f`. */
    val volume: Float

    /** The current repeat mode. */
    val repeatMode: VideoPlayerRepeatMode

    /** `true` if the playback queue is shuffled. */
    val shuffleModeEnabled: Boolean

    /** `true` if the player should pause at the end of each media item. */
    val pauseAtEndOfMediaItems: Boolean

    /** The number of media items in the current queue. */
    val mediaItemCount: Int

    /** The index of the current media item in the queue. */
    val currentMediaItemIndex: Int

    /** The index of the next media item, or `-1` if there is none. */
    val nextMediaItemIndex: Int

    /** The index of the previous media item, or `-1` if there is none. */
    val previousMediaItemIndex: Int

    /** `true` if there is a next media item in the current queue. */
    val hasNextMediaItem: Boolean

    /** `true` if there is a previous media item in the current queue. */
    val hasPreviousMediaItem: Boolean

    /** The backward seek increment in milliseconds, configured when the player is created. */
    val seekBackIncrementMs: Long

    /** The forward seek increment in milliseconds, configured when the player is created. */
    val seekForwardIncrementMs: Long

    /**
     * The position threshold used by [seekToPrevious] to choose between restarting the current
     * video and moving to the previous item.
     */
    val maxSeekToPreviousPositionMs: Long

    /** `true` if the current media item supports seeking. */
    val isCurrentMediaItemSeekable: Boolean

    /** `true` if the current media item is a live stream. */
    val isCurrentMediaItemLive: Boolean

    /** `true` if the timeline of the current media item can change. */
    val isCurrentMediaItemDynamic: Boolean

    /** Sets a single source for playback. */
    fun setSource(source: VideoPlayerSource)

    /** Sets a single source and the initial playback position. */
    fun setSource(source: VideoPlayerSource, startPositionMs: Long)

    /** Sets a single source, optionally preserving the current playback position. */
    fun setSource(source: VideoPlayerSource, resetPosition: Boolean)

    /** Sets multiple sources as the playback queue. */
    fun setSources(sources: List<VideoPlayerSource>)

    /** Sets the queue, optionally preserving the current item index and playback position. */
    fun setSources(sources: List<VideoPlayerSource>, resetPosition: Boolean)

    /** Sets the queue, the initial media item, and the position within it. */
    fun setSources(
        sources: List<VideoPlayerSource>,
        startMediaItemIndex: Int,
        startPositionMs: Long,
    )

    /** Appends a source to the current queue. */
    fun addSource(source: VideoPlayerSource)

    /** Inserts a source at the specified position in the current queue. */
    fun addSource(index: Int, source: VideoPlayerSource)

    /** Appends sources to the current queue. */
    fun addSources(sources: List<VideoPlayerSource>)

    /** Inserts sources at the specified position in the current queue. */
    fun addSources(index: Int, sources: List<VideoPlayerSource>)

    /** Moves a source to a different position in the queue. */
    fun moveSource(currentIndex: Int, newIndex: Int)

    /** Moves the range of sources `[fromIndex, toIndex)` to a new position. */
    fun moveSources(fromIndex: Int, toIndex: Int, newIndex: Int)

    /** Replaces the source at the specified index. */
    fun replaceSource(index: Int, source: VideoPlayerSource)

    /** Replaces the range of sources `[fromIndex, toIndex)` with a new list. */
    fun replaceSources(
        fromIndex: Int,
        toIndex: Int,
        sources: List<VideoPlayerSource>,
    )

    /** Removes the source at the specified index. */
    fun removeSource(index: Int)

    /** Removes the range of sources `[fromIndex, toIndex)`. */
    fun removeSources(fromIndex: Int, toIndex: Int)

    /** Prepares the current sources for playback. */
    fun prepare()

    /** Starts or resumes playback. */
    fun play()

    /** Pauses playback. */
    fun pause()

    /** Sets whether the player should start playback as soon as it is ready. */
    fun setPlayWhenReady(playWhenReady: Boolean)

    /** Stops playback. */
    fun stop()

    /** Seeks to the specified playback position in milliseconds. */
    fun seekTo(positionMs: Long)

    /** Seeks to the specified media item and position within it. */
    fun seekTo(mediaItemIndex: Int, positionMs: Long)

    /** Seeks to the default position of the current media item. */
    fun seekToDefaultPosition()

    /** Seeks to the default position of the specified media item. */
    fun seekToDefaultPosition(mediaItemIndex: Int)

    /** Seeks backward in the current video by [seekBackIncrementMs]. */
    fun seekBack()

    /** Seeks forward in the current video by [seekForwardIncrementMs]. */
    fun seekForward()

    /** Switches to the previous media item in the queue. */
    fun seekToPreviousMediaItem()

    /** Switches to the next media item in the queue. */
    fun seekToNextMediaItem()

    /** Restarts the current video or switches to the previous one based on the current position. */
    fun seekToPrevious()

    /** Switches to the next media item or seeks to the current live position. */
    fun seekToNext()

    /** Enables or disables repeating the current media item or the entire queue. */
    fun setRepeatMode(repeatMode: VideoPlayerRepeatMode)

    /** Enables or disables shuffling the playback queue. */
    fun setShuffleModeEnabled(shuffleModeEnabled: Boolean)

    /** Sets whether playback should pause at the end of each media item. */
    fun setPauseAtEndOfMediaItems(pauseAtEndOfMediaItems: Boolean)

    /** Changes the playback speed. The value must be greater than `0f`. */
    fun setPlaybackSpeed(speed: Float)

    /** Sets the player volume, ranging from `0f` to `1f`. */
    fun setVolume(volume: Float)

    /** Mutes the player. */
    fun mute()

    /** Restores the volume that was set before [mute]. */
    fun unmute()

    /** Removes all sources from the current player. */
    fun clearSources()

    /** Registers a listener for player events. */
    fun addListener(listener: VideoPlayerListener)

    /** Removes a previously registered listener. */
    fun removeListener(listener: VideoPlayerListener)

    /**
     * Schedules [action] to run at the specified playback position.
     *
     * Useful for haptic feedback, analytics, or synchronizing the UI with a specific
     * moment in the video.
     *
     * @param position the playback position at which to invoke [action].
     * @param mediaItemIndex the media item index in the queue. If `null`, the current item is used.
     * @param deleteAfterDelivery if `true`, the action is automatically removed after its first invocation.
     * @param action the code to execute at the specified playback position.
     * @return a handle that can be used to cancel the scheduled action.
     */
    fun scheduleActionAt(
        position: Duration,
        mediaItemIndex: Int? = null,
        deleteAfterDelivery: Boolean = true,
        action: () -> Unit,
    ): VideoPlayerScheduledAction

    /** Releases the player resources. The instance must not be used after this call. */
    fun release()
}
