package io.github.danielbul464.androidcodingrecipes.exo_player.core.domain

import android.net.Uri
import androidx.core.net.toUri
import androidx.media3.common.Player

/**
 * A video source for [VideoPlayer].
 *
 * All source types are converted to a [Uri] consumed by the concrete player implementation.
 */
sealed interface VideoPlayerSource {

    /** The URI to pass to the player. */
    val uri: Uri

    /** A video from the application's `assets/` directory. */
    data class Asset(private val fileName: String) : VideoPlayerSource {
        override val uri: Uri = "asset:///${fileName.trimStart('/')}".toUri()
    }

    /** A video specified by a network URL or another URL string. */
    data class Url(private val value: String) : VideoPlayerSource {
        override val uri: Uri = value.toUri()
    }

    /** A prebuilt [Uri] supplied by the caller. */
    data class UriSource(override val uri: Uri) : VideoPlayerSource

    /** A video from `res/raw`. */
    data class RawResource(private val resourceId: Int) : VideoPlayerSource {
        override val uri: Uri = "rawresource:///$resourceId".toUri()
    }
}

/** The playback repeat mode. */
enum class VideoPlayerRepeatMode(internal val platformValue: Int) {
    Off(Player.REPEAT_MODE_OFF),
    One(Player.REPEAT_MODE_ONE),
    All(Player.REPEAT_MODE_ALL),
}

/** The precision used when seeking to a specified position. */
enum class VideoPlayerSeekMode {
    Exact,
    PreviousSync,
    NextSync,
    ClosestSync,
}

/** The playback state. */
enum class VideoPlaybackState {
    Idle,
    Buffering,
    Ready,
    Ended,
}

/** The reason for a change in `playWhenReady`. */
enum class VideoPlayerPlayWhenReadyChangeReason {
    UserRequest,
    AudioFocusLoss,
    AudioBecomingNoisy,
    Remote,
    EndOfMediaItem,
    SuppressedTooLong,
    Unknown,
}

/** The reason for a transition between queue items. */
enum class VideoPlayerMediaItemTransitionReason {
    Repeat,
    Auto,
    Seek,
    PlaylistChanged,
    Unknown,
}

/** The reason for a discontinuous change in playback position. */
enum class VideoPlayerPositionDiscontinuityReason {
    AutoTransition,
    Seek,
    SeekAdjustment,
    Skip,
    Remove,
    Internal,
    SilenceSkip,
    Unknown,
}

/** The playback position within a queue item. */
data class VideoPlayerPositionInfo(
    val mediaItemIndex: Int,
    val positionMs: Long,
    val contentPositionMs: Long,
)

/** A playback error. */
data class VideoPlayerError(
    val errorCode: Int,
    val errorCodeName: String,
    val message: String?,
    val cause: Throwable?,
)
