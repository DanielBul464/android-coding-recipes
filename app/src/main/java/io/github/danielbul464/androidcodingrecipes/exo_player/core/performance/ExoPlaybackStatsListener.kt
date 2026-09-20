package io.github.danielbul464.androidcodingrecipes.exo_player.core.performance

import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.analytics.PlaybackStats
import androidx.media3.exoplayer.analytics.PlaybackStatsListener
import java.util.Locale

@OptIn(UnstableApi::class)
internal fun createPlaybackStatsListener(
    config: VideoPlaybackAnalyticsConfig,
): PlaybackStatsListener {
    check(config.isEnabled) { "Playback stats listener must not be created when disabled" }

    return PlaybackStatsListener(/* keepHistory = */ false) { _, playbackStats ->
        val metrics = playbackStats.toVideoPlaybackMetrics(config.tag)
        Log.d(metrics.tag, metrics.toLogMessage())
        config.onMetricsChanged?.invoke(metrics)
    }
}

@OptIn(UnstableApi::class)
internal fun PlaybackStats.toVideoPlaybackMetrics(tag: String): VideoPlaybackMetrics =
    VideoPlaybackMetrics(
        tag = tag,
        elapsedTimeMs = totalElapsedTimeMs,
        playTimeMs = totalPlayTimeMs,
        joinTimeMs = meanJoinTimeMs.takeUnless { it == C.TIME_UNSET },
        rebufferCount = totalRebufferCount,
        rebufferTimeMs = totalRebufferTimeMs,
        maxRebufferTimeMs = maxRebufferTimeMs.takeUnless { it == C.TIME_UNSET },
        rebufferTimeRatio = rebufferTimeRatio,
        droppedFrames = totalDroppedFrames,
        droppedFramesRate = droppedFramesRate,
        bandwidthBytes = totalBandwidthBytes,
        meanBandwidthBps = meanBandwidth.takeUnless { it == C.LENGTH_UNSET },
        initialVideoHeight = meanInitialVideoFormatHeight.takeUnless { it == C.LENGTH_UNSET },
        initialVideoBitrateBps =
            meanInitialVideoFormatBitrate.takeUnless { it == C.LENGTH_UNSET },
        fatalErrorCount = fatalErrorCount,
        nonFatalErrorCount = nonFatalErrorCount,
        ended = endedCount > 0,
        abandonedBeforeReady = abandonedBeforeReadyCount > 0,
    )

internal fun VideoPlaybackMetrics.toLogMessage(): String =
    buildString {
        append("elapsedTimeMs=").append(elapsedTimeMs)
        append(", playTimeMs=").append(playTimeMs)
        append(", joinTimeMs=").append(joinTimeMs.toLogValue())
        append(", rebufferCount=").append(rebufferCount)
        append(", rebufferTimeMs=").append(rebufferTimeMs)
        append(", maxRebufferTimeMs=").append(maxRebufferTimeMs.toLogValue())
        append(", rebufferTimeRatio=").append(rebufferTimeRatio.toLogValue())
        append(", droppedFrames=").append(droppedFrames)
        append(", droppedFramesRate=").append(droppedFramesRate.toLogValue())
        append(", bandwidthBytes=").append(bandwidthBytes)
        append(", meanBandwidthBps=").append(meanBandwidthBps.toLogValue())
        append(", initialVideoHeight=").append(initialVideoHeight.toLogValue())
        append(", initialVideoBitrateBps=").append(initialVideoBitrateBps.toLogValue())
        append(", fatalErrorCount=").append(fatalErrorCount)
        append(", nonFatalErrorCount=").append(nonFatalErrorCount)
        append(", ended=").append(ended)
        append(", abandonedBeforeReady=").append(abandonedBeforeReady)
    }

private fun Long?.toLogValue(): String = this?.toString() ?: UNAVAILABLE_VALUE

private fun Int?.toLogValue(): String = this?.toString() ?: UNAVAILABLE_VALUE

private fun Float.toLogValue(): String = String.format(Locale.US, "%.3f", this)

private const val UNAVAILABLE_VALUE = "n/a"
