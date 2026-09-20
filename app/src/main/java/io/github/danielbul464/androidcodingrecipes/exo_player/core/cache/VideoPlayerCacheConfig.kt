package io.github.danielbul464.androidcodingrecipes.exo_player.core.cache

/**
 * Configuration for the shared video player cache.
 *
 * Configuration values must be provided by an application-specific module.
 *
 * @param directoryName the name of a dedicated directory within the application's cache directory.
 * @param maxSizeBytes the maximum cache size in bytes.
 * @param ignoreCacheOnError allows reading from the upstream source after a cache error.
 * @param isLoggingEnabled enables cache logging.
 */
data class VideoPlayerCacheConfig(
    val directoryName: String,
    val maxSizeBytes: Long,
    val ignoreCacheOnError: Boolean = true,
    val isLoggingEnabled: Boolean = false,
) {

    init {
        require(directoryName.isNotBlank()) { "Cache directory name must not be blank" }
        require('/' !in directoryName && '\\' !in directoryName) {
            "Cache directory name must not contain path separators"
        }
        require(maxSizeBytes > 0) { "Cache max size must be positive" }
    }
}
