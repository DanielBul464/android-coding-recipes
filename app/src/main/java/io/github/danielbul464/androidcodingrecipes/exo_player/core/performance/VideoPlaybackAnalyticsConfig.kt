package io.github.danielbul464.androidcodingrecipes.exo_player.core.performance

/**
 * Controls playback logging and optional metric updates.
 *
 * @param onMetricsChanged receives final metrics when a playback session ends, for example when
 *   its source is removed or the player is released. Called on the player's application thread.
 */
data class VideoPlaybackAnalyticsConfig(
    val isEnabled: Boolean,
    val tag: String = DEFAULT_TAG,
    val onMetricsChanged: ((VideoPlaybackMetrics) -> Unit)? = null,
) {

    init {
        require(tag.isNotBlank()) { "Playback analytics tag must not be blank" }
    }

    companion object {
        const val DEFAULT_TAG = "VideoPlaybackMetrics"

        val Disabled = VideoPlaybackAnalyticsConfig(isEnabled = false)
    }
}
