package io.github.danielbul464.androidcodingrecipes.exo_player.core.performance

/**
 * Summary metrics for a single completed playback session, calculated by Media3.
 *
 * @param tag the Logcat tag. Separates metrics for a specific player use case from other
 *   application logs.
 * @param elapsedTimeMs the total session lifetime in milliseconds across all states, including
 *   playback, waiting, and pauses. Helps assess the duration of the observed session.
 * @param playTimeMs the active playback time in milliseconds. Provides a baseline for comparing
 *   rebuffer counts and dropped frames across sessions of different lengths.
 * @param joinTimeMs the initial preparation time until playback is ready, in milliseconds.
 *   Indicates how quickly the video starts: a lower value means the user sees content sooner.
 *   Absent if Media3 could not determine a valid startup time.
 * @param rebufferCount the number of rebuffers after initial startup, excluding buffering after
 *   seeking. Higher values indicate unstable data loading or an insufficient buffer size.
 * @param rebufferTimeMs the total rebuffering time in milliseconds. Shows how long the user
 *   waited for playback to resume after it had already started.
 * @param maxRebufferTimeMs the longest rebuffer duration in milliseconds. Helps identify isolated
 *   but noticeable stalls. Absent if no rebuffering occurred.
 * @param rebufferTimeRatio the fraction of time spent rebuffering relative to active playback
 *   and waiting time. Useful for comparing sessions of different lengths: values closer to zero
 *   indicate more stable playback.
 * @param droppedFrames the total number of frames the video renderer failed to display in time.
 *   This absolute value should be assessed together with [playTimeMs].
 * @param droppedFramesRate the average number of dropped frames per second of active playback.
 *   Helps identify load on the decoder, GPU, or main thread regardless of video duration.
 * @param bandwidthBytes the number of bytes downloaded from the network during the session.
 *   Helps compare network usage before and after optimizations, but does not represent the
 *   total video size or the amount of data read from the cache.
 * @param meanBandwidthBps the average network download speed in bits per second. Helps determine
 *   whether delays are caused by a slow connection. Absent if the speed was not measured.
 * @param initialVideoHeight the height of the initially selected video format in pixels.
 *   Helps relate startup time and dropped frames to the actual video resolution.
 *   Absent if the format was not determined.
 * @param initialVideoBitrateBps the bitrate of the initially selected video format in bits per
 *   second. Helps assess whether network and decoder load matches the source video quality.
 *   Absent if the bitrate was not determined.
 * @param fatalErrorCount the number of errors that stopped playback. Higher values indicate
 *   reduced reliability of the player or data source.
 * @param nonFatalErrorCount the number of recoverable errors after which playback could continue.
 *   Helps reveal underlying issues that do not directly end the session.
 * @param ended `true` if the session reached the end of the media content at least once.
 *   Distinguishes normal completion from the user leaving, a source change, or an error.
 * @param abandonedBeforeReady `true` if the session ended before the player became ready.
 *   Helps identify slow startup and, in carousels, distinguish a quick user swipe from a
 *   playback error.
 */
data class VideoPlaybackMetrics(
    val tag: String,
    val elapsedTimeMs: Long,
    val playTimeMs: Long,
    val joinTimeMs: Long?,
    val rebufferCount: Int,
    val rebufferTimeMs: Long,
    val maxRebufferTimeMs: Long?,
    val rebufferTimeRatio: Float,
    val droppedFrames: Long,
    val droppedFramesRate: Float,
    val bandwidthBytes: Long,
    val meanBandwidthBps: Int?,
    val initialVideoHeight: Int?,
    val initialVideoBitrateBps: Int?,
    val fatalErrorCount: Int,
    val nonFatalErrorCount: Int,
    val ended: Boolean,
    val abandonedBeforeReady: Boolean,
)
