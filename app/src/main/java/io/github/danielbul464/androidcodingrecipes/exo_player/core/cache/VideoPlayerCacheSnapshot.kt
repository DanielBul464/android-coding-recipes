package io.github.danielbul464.androidcodingrecipes.exo_player.core.cache

/**
 * A snapshot of the shared video player cache state.
 *
 * @param usedSizeBytes the current size of stored data in bytes.
 * @param maxSizeBytes the configured maximum cache size in bytes.
 * @param cachedResourcesCount the number of fully or partially cached resources.
 */
data class VideoPlayerCacheSnapshot(
    val usedSizeBytes: Long,
    val maxSizeBytes: Long,
    val cachedResourcesCount: Int,
)
