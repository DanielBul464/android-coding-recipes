package io.github.danielbul464.androidcodingrecipes.exo_player.core.cache

/** Administrative operations for the shared video player cache. */
interface VideoPlayerCacheManager {

    /** Removes all stored data from the cache without changing its configuration. */
    suspend fun clear()

    /** @return the current cache state. */
    suspend fun snapshot(): VideoPlayerCacheSnapshot
}
