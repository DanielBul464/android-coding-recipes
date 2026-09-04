package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.multi_back_stack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntryDecorator

@Composable
fun rememberMultiBackStackNavigator(
    state: MultiBackStackNavigationState,
): MultiBackStackNavigator =
    remember(state) {
        MultiBackStackNavigator(state)
    }

@Composable
fun <T : Any> rememberMultiBackStackNavEntryDecorator(
    multiBackStackNavigator: MultiBackStackNavigator,
): NavEntryDecorator<T> =
    remember(multiBackStackNavigator) {
        NavEntryDecorator(
            onPop = {},
            decorate = { entry ->
                CompositionLocalProvider(
                    LocalMultiBackStackNavigator provides multiBackStackNavigator,
                ) {
                    entry.Content()
                }
            },
        )
    }
