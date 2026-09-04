package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.app_navigator

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAppNavigator = staticCompositionLocalOf<AppNavigator> {
    error("No AppNavigator provided. Wrap the content with CompositionLocalProvider.")
}
