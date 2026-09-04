package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.Route
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.TopLevelRoute
import kotlinx.serialization.Serializable

/** Describes the destinations available inside the application's main flow. */
sealed interface MainFlowDestination : Route {

    @Serializable
    data object Home1 : MainFlowDestination, TopLevelRoute

    @Serializable
    data object Home2 : MainFlowDestination

    @Serializable
    data object Catalog1 : MainFlowDestination, TopLevelRoute

    @Serializable
    data object Catalog2 : MainFlowDestination

    @Serializable
    data object Login1 : MainFlowDestination, TopLevelRoute

    @Serializable
    data object Login2 : MainFlowDestination

    @Serializable
    data object Profile1 : MainFlowDestination, TopLevelRoute

    @Serializable
    data object Profile2 : MainFlowDestination
}
