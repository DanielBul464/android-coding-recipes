package io.github.danielbul464.androidcodingrecipes.exo_player.cache_recipe

import io.github.danielbul464.androidcodingrecipes.exo_player.core.cache.VideoPlayerCache

/** Provides the application's configured shared video player cache. */
interface VideoPlayerCacheProvider {

    val cache: VideoPlayerCache
}
