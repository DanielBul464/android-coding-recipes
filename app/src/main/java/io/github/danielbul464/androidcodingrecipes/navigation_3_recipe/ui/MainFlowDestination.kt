package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.Route
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.TopLevelRoute
import kotlinx.serialization.Serializable
import java.util.UUID

/** Describes the destinations available inside the application's main flow. */
sealed interface MainFlowDestination : Route {

    @Serializable
    data class Home1(
        val instanceId: String = newInstanceId(),
    ) : MainFlowDestination, TopLevelRoute

    @Serializable
    data object Home2 : MainFlowDestination

    @Serializable
    data class Catalog1(
        val instanceId: String = newInstanceId(),
    ) : MainFlowDestination, TopLevelRoute

    @Serializable
    data object Catalog2 : MainFlowDestination

    @Serializable
    data class Login1(
        val instanceId: String = newInstanceId(),
    ) : MainFlowDestination, TopLevelRoute

    @Serializable
    data object Login2 : MainFlowDestination

    @Serializable
    data class Profile1(
        val instanceId: String = newInstanceId(),
    ) : MainFlowDestination, TopLevelRoute

    @Serializable
    data object Profile2 : MainFlowDestination
}

private fun newInstanceId(): String = UUID.randomUUID().toString()
