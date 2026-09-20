package io.github.danielbul464.androidcodingrecipes.exo_player.cache_recipe.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.danielbul464.androidcodingrecipes.exo_player.core.VideoPlayerBuilder
import io.github.danielbul464.androidcodingrecipes.exo_player.core.cache.VideoPlayerCache
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayerSource
import io.github.danielbul464.androidcodingrecipes.exo_player.core.ui.VideoPlayerSurface

/**
 * Plays the same video with and without a disk cache and logs session metrics.
 *
 * The first visit fills an empty cache. Removing this screen from composition releases the players
 * and logs session metrics tagged `VideoWithCache` and `VideoWithoutCache`. Reopening the screen
 * creates new players that use the shared disk cache rather than existing playback buffers.
 * Both players use the same default buffering settings. The top player uses the cache.
 * Join time measures initial preparation until playback is ready, not the full download duration.
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
            .setPlaybackAnalyticsEnabled(enabled = true, tag = "VideoWithCache")
            .build()
    }
    val networkPlayer = remember(context, videoUrl) {
        VideoPlayerBuilder(context)
            .setPlayWhenReady(true)
            .setVolume(0f)
            .setPlaybackAnalyticsEnabled(enabled = true, tag = "VideoWithoutCache")
            .build()
    }

    DisposableEffect(cachedPlayer) {
        cachedPlayer.setSource(VideoPlayerSource.Url(videoUrl))
        cachedPlayer.prepare()

        onDispose {
            cachedPlayer.release()
        }
    }

    DisposableEffect(networkPlayer) {
        networkPlayer.setSource(VideoPlayerSource.Url(videoUrl))
        networkPlayer.prepare()

        onDispose {
            networkPlayer.release()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        VideoPlayerSurface(
            player = cachedPlayer,
            modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black),
        )
        VideoPlayerSurface(
            player = networkPlayer,
            modifier = Modifier.fillMaxWidth().weight(1f).background(Color.Black),
        )
    }
}

private const val SAMPLE_VIDEO_URL =
    "https://g6.akbrilliant.ru/media/generic/0c9a964ec657141c4337f2e2ff38e88ec1665fb4.mp4"
