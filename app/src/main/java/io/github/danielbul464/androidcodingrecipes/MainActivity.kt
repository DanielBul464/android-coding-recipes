package io.github.danielbul464.androidcodingrecipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui.AppContent
import io.github.danielbul464.androidcodingrecipes.presentation.AndroidRecipesTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidRecipesTheme {
                AppContent()
            }
        }
    }
}
