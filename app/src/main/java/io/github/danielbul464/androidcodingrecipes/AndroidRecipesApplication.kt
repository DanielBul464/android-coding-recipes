package io.github.danielbul464.androidcodingrecipes

import android.app.Application
import io.github.danielbul464.androidcodingrecipes.exo_player.cache_recipe.VideoPlayerCacheImpl

class AndroidRecipesApplication : Application() {

    val videoPlayerCache by lazy { VideoPlayerCacheImpl(this) }
}
