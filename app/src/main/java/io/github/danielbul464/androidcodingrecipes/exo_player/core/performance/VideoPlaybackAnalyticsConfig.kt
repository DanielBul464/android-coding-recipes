package io.github.danielbul464.androidcodingrecipes.exo_player.core.performance

data class VideoPlaybackAnalyticsConfig(
    val isEnabled: Boolean,
    val tag: String = DEFAULT_TAG,
) {

    init {
        require(tag.isNotBlank()) { "Playback analytics tag must not be blank" }
    }

    companion object {
        const val DEFAULT_TAG = "VideoPlaybackMetrics"

        val Disabled = VideoPlaybackAnalyticsConfig(isEnabled = false)
    }
}
