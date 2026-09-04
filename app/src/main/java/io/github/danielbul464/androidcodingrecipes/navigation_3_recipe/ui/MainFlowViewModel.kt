package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import androidx.lifecycle.ViewModel

class MainFlowViewModel : ViewModel() {

    val tabs: List<AppTabBarItem> = listOf(
        AppTabBarItem.Home,
        AppTabBarItem.Catalog,
        AppTabBarItem.Login,
        AppTabBarItem.Profile,
    )
}
