package io.github.danielbul464.androidcodingrecipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.danielbul464.androidcodingrecipes.exo_player.cache_recipe.ui.VideoCacheComparisonScreen
import io.github.danielbul464.androidcodingrecipes.presentation.AndroidRecipesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cache = (application as AndroidRecipesApplication).videoPlayerCache

        setContent {
            AndroidRecipesTheme {
                VideoCacheComparisonScreen(cache = cache)
            }
        }
    }
}
