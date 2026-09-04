package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainFlowViewModel(
    isUserLoggedIn: Boolean = false,
) : ViewModel() {

    var isUserLoggedIn by mutableStateOf(isUserLoggedIn)
        private set

    val allTabs: List<AppTabBarItem> = listOf(
        AppTabBarItem.Home,
        AppTabBarItem.Catalog,
        AppTabBarItem.Login,
        AppTabBarItem.Profile,
    )

    private val loggedInTabs = listOf(
        AppTabBarItem.Home,
        AppTabBarItem.Catalog,
        AppTabBarItem.Profile,
    )

    private val loggedOutTabs = listOf(
        AppTabBarItem.Home,
        AppTabBarItem.Catalog,
        AppTabBarItem.Login,
    )

    val tabs: List<AppTabBarItem>
        get() = if (isUserLoggedIn) loggedInTabs else loggedOutTabs

    fun logIn() {
        isUserLoggedIn = true
    }

    fun logOut() {
        isUserLoggedIn = false
    }
}
