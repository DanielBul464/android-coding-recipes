package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.app_navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey

@Composable
fun rememberAppNavigator(
    backStack: NavBackStack<NavKey>,
): AppNavigator =
    remember(backStack) {
        AppNavigator(backStack)
    }

@Composable
fun <T : Any> rememberAppNavigatorNavEntryDecorator(
    appNavigator: AppNavigator,
): NavEntryDecorator<T> =
    remember(appNavigator) {
        NavEntryDecorator(
            onPop = {},
            decorate = { entry ->
                CompositionLocalProvider(
                    LocalAppNavigator provides appNavigator,
                ) {
                    entry.Content()
                }
            },
        )
    }
