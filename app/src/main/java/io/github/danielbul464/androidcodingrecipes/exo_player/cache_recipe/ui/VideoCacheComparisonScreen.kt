package io.github.danielbul464.androidcodingrecipes.exo_player.cache_recipe.ui

import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.danielbul464.androidcodingrecipes.R
import io.github.danielbul464.androidcodingrecipes.exo_player.core.VideoPlayerBuilder
import io.github.danielbul464.androidcodingrecipes.exo_player.core.cache.VideoPlayerCache
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayer
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerListener
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerSource
import io.github.danielbul464.androidcodingrecipes.exo_player.core.ui.VideoPlayerSurface

/**
 * Shows and logs time to the first video frame with and without a disk cache.
 *
 * Each measurement starts when the source is set and finishes when the first frame is rendered.
 * Results appear immediately in the UI and logs tagged `VideoWithCache` and `VideoWithoutCache`.
 * This includes loading, preparation, and rendering, and differs from Media3's session join time.
 *
 * The first visit fills an empty cache. Reopening the screen creates new players that use the
 * shared disk cache rather than existing playback buffers. Removing the screen releases the players
 * and logs the full session metrics as before.
 * Both players use the same default buffering settings. The top player uses the cache.
 *
 * @param cache the shared application cache, created once outside this screen and reused on return.
 * @param videoUrl the same video URL used by both players.
 */
@Composable
fun VideoCacheComparisonScreen(
    cache: VideoPlayerCache,
    modifier: Modifier = Modifier,
    videoUrl: String = SAMPLE_VIDEO_URL,
) {
    val context = LocalContext.current.applicationContext

    val cachedPlayer = remember(context, videoUrl, cache) {
        VideoPlayerBuilder(context)
            .setCache(cache)
            .setPlayWhenReady(true)
            .setVolume(0f)
            .setPlaybackAnalyticsEnabled(enabled = true, tag = WITH_CACHE_LOG_TAG)
            .build()
    }
    val networkPlayer = remember(context, videoUrl) {
        VideoPlayerBuilder(context)
            .setPlayWhenReady(true)
            .setVolume(0f)
            .setPlaybackAnalyticsEnabled(enabled = true, tag = WITHOUT_CACHE_LOG_TAG)
            .build()
    }

    val cachedStartupMs = rememberTimeToFirstFrameMs(cachedPlayer, videoUrl, WITH_CACHE_LOG_TAG)
    val networkStartupMs = rememberTimeToFirstFrameMs(networkPlayer, videoUrl, WITHOUT_CACHE_LOG_TAG)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.video_cache_first_frame, cachedStartupMs?.toString() ?: "—"),
            color = MaterialTheme.colorScheme.onBackground,
        )
        VideoPlayerSurface(
            player = cachedPlayer,
            modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black),
        )
        Text(
            text = stringResource(R.string.video_network_first_frame, networkStartupMs?.toString() ?: "—"),
            color = MaterialTheme.colorScheme.onBackground,
        )
        VideoPlayerSurface(
            player = networkPlayer,
            modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black),
        )
    }
}

/** Starts playback, measures its first frame once, and releases the player when disposed. */
@Composable
private fun rememberTimeToFirstFrameMs(
    player: VideoPlayer,
    videoUrl: String,
    logTag: String,
): Long? {
    val timeToFirstFrameMs = remember(player, videoUrl) { mutableStateOf<Long?>(null) }

    DisposableEffect(player, videoUrl) {
        var startedAtMs = 0L
        val listener = object : VideoPlayerListener {
            override fun onRenderedFirstFrame() {
                // A new surface may trigger this event again; keep the initial startup measurement.
                if (timeToFirstFrameMs.value != null) return
                val elapsedMs = SystemClock.elapsedRealtime() - startedAtMs
                timeToFirstFrameMs.value = elapsedMs
                Log.d(logTag, "timeToFirstFrameMs=$elapsedMs")
            }
        }
        player.addListener(listener)
        startedAtMs = SystemClock.elapsedRealtime()
        player.setSource(VideoPlayerSource.Url(videoUrl))
        player.prepare()

        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }

    return timeToFirstFrameMs.value
}

private const val WITH_CACHE_LOG_TAG = "VideoWithCache"
private const val WITHOUT_CACHE_LOG_TAG = "VideoWithoutCache"
private const val SAMPLE_VIDEO_URL =
    "https://g6.akbrilliant.ru/media/generic/0c9a964ec657141c4337f2e2ff38e88ec1665fb4.mp4"
