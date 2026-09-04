package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(AppDestination.CustomSplash)

    NavDisplay(
        backStack = backStack,
        modifier = modifier.fillMaxSize(),
        onBack = { TODO("Handle back navigation") },
        entryProvider = entryProvider {
            entry<AppDestination.CustomSplash> {
                RecipeScreen(
                    buttonText = "Open onboarding",
                    onButtonClick = { TODO("Navigate to onboarding") },
                )
            }

            entry<AppDestination.Onboarding> {
                RecipeScreen(
                    buttonText = "Open main flow",
                    onButtonClick = { TODO("Navigate to the main flow") },
                )
            }

            entry<AppDestination.MainFlow> {
                RecipeScreen(
                    buttonText = "Navigate in main flow",
                    onButtonClick = { TODO("Navigate inside the main flow") },
                )
            }
        },
    )
}

@Composable
private fun RecipeScreen(
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick = onButtonClick) {
            Text(text = buttonText)
        }
    }
}
