package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.Route
import kotlinx.serialization.Serializable

/** Describes the destinations in the application's outer navigation flow. */
sealed interface AppDestination : Route {

    @Serializable
    data object CustomSplash : AppDestination

    @Serializable
    data object Onboarding : AppDestination

    @Serializable
    data object MainFlow : AppDestination
}
