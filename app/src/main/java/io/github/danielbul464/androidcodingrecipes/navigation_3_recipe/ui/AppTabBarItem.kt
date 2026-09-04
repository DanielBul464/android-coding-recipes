package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.TopLevelRoute

sealed class AppTabBarItem(
    val label: String,
    val route: TopLevelRoute,
) {

    data object Home : AppTabBarItem(
        label = "Home",
        route = MainFlowDestination.Home1(),
    )

    data object Catalog : AppTabBarItem(
        label = "Catalog",
        route = MainFlowDestination.Catalog1(),
    )

    data object Login : AppTabBarItem(
        label = "Login",
        route = MainFlowDestination.Login1(),
    )

    data object Profile : AppTabBarItem(
        label = "Profile",
        route = MainFlowDestination.Profile1(),
    )

    fun createRoute(): TopLevelRoute = when (this) {
        Home -> MainFlowDestination.Home1()
        Catalog -> MainFlowDestination.Catalog1()
        Login -> MainFlowDestination.Login1()
        Profile -> MainFlowDestination.Profile1()
    }
}
