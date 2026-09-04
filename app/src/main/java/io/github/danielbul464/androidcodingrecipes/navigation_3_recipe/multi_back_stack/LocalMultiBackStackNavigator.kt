package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.multi_back_stack

import androidx.compose.runtime.staticCompositionLocalOf

val LocalMultiBackStackNavigator = staticCompositionLocalOf<MultiBackStackNavigator> {
    error(
        "No MultiBackStackNavigator provided. Wrap the content with CompositionLocalProvider.",
    )
}
