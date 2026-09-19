package io.github.danielbul464.androidcodingrecipes.exo_player.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import io.github.danielbul464.androidcodingrecipes.exo_player.core.ExoVideoPlayer
import io.github.danielbul464.androidcodingrecipes.exo_player.core.domain.VideoPlayer

@Composable
fun VideoPlayerSurface(
    player: VideoPlayer,
    modifier: Modifier = Modifier,
    surfaceType: VideoPlayerSurfaceType = VideoPlayerSurfaceType.SurfaceView,
) {
    val platformPlayer = player.requirePlatformPlayer()

    PlayerSurface(
        player = platformPlayer,
        modifier = modifier,
        surfaceType = surfaceType.platformValue,
    )
}

@Composable
fun VideoPlayerContentFrame(
    player: VideoPlayer,
    modifier: Modifier = Modifier,
    surfaceType: VideoPlayerSurfaceType = VideoPlayerSurfaceType.SurfaceView,
    contentScale: ContentScale = ContentScale.Fit,
    keepContentOnReset: Boolean = false,
    shutter: @Composable () -> Unit = { Box(Modifier.fillMaxSize().background(Color.Black)) }
) {
    val platformPlayer = player.requirePlatformPlayer()

    ContentFrame(
        player = platformPlayer,
        modifier = modifier,
        surfaceType = surfaceType.platformValue,
        contentScale = contentScale,
        keepContentOnReset = keepContentOnReset,
        shutter = shutter
    )
}

enum class VideoPlayerSurfaceType(internal val platformValue: Int) {
    SurfaceView(SURFACE_TYPE_SURFACE_VIEW),
    TextureView(SURFACE_TYPE_TEXTURE_VIEW),
}

private fun VideoPlayer.requirePlatformPlayer() =
    (this as? ExoVideoPlayer)?.asPlatformPlayer()
        ?: error("Unsupported VideoPlayer implementation: ${this::class.qualifiedName}")
