package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.TopLevelRoute
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.multi_back_stack.BackStackPolicy
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.multi_back_stack.rememberMultiBackStackNavigationState
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.multi_back_stack.rememberMultiBackStackNavigator

@Composable
fun MainFlowContent(
    modifier: Modifier = Modifier,
    mainFlowViewModel: MainFlowViewModel = viewModel(),
) {
    val topLevelRoutes = remember(mainFlowViewModel) {
        mainFlowViewModel.tabs.mapTo(mutableSetOf()) { tab -> tab.route }
    }
    val navigationState = rememberMultiBackStackNavigationState(
        startRoute = AppTabBarItem.Home.route,
        topLevelRoutes = topLevelRoutes,
    )
    val navigator = rememberMultiBackStackNavigator(navigationState)
    val entryProvider = remember { mainFlowEntryProvider() }
    val entries = navigationState.toDecoratedEntries(
        multiBackStackNavigator = navigator,
        entryProvider = entryProvider,
    )
    val selectedTab = mainFlowViewModel.tabs.firstOrNull { tab ->
        tab.route::class == navigationState.topLevelRoute::class
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorScheme.background,
        bottomBar = {
            NavigationBar {
                mainFlowViewModel.tabs.forEach { tab ->
                    val isSelected = tab == selectedTab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navigator.navigate(
                                route = tab.route,
                                backStackPolicy = if (isSelected) {
                                    BackStackPolicy.PopToRoot
                                } else {
                                    BackStackPolicy.Preserve
                                },
                            )
                        },
                        icon = { Text(text = tab.label.take(1)) },
                        label = { Text(text = tab.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            entries = entries,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            onBack = navigator::goBack,
        )
    }
}

private fun mainFlowEntryProvider(): (NavKey) -> NavEntry<NavKey> =
    entryProvider {
        entry<MainFlowDestination.Home1> {
            MainFlowScreen("Home 1")
        }

        entry<MainFlowDestination.Home2> {
            MainFlowScreen("Home 2")
        }

        entry<MainFlowDestination.Catalog1> {
            MainFlowScreen("Catalog 1")
        }

        entry<MainFlowDestination.Catalog2> {
            MainFlowScreen("Catalog 2")
        }

        entry<MainFlowDestination.Login1> {
            MainFlowScreen("Login 1")
        }

        entry<MainFlowDestination.Login2> {
            MainFlowScreen("Login 2")
        }

        entry<MainFlowDestination.Profile1> {
            MainFlowScreen("Profile 1")
        }

        entry<MainFlowDestination.Profile2> {
            MainFlowScreen("Profile 2")
        }
    }

@Composable
private fun MainFlowScreen(
    name: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick = { TODO("Navigate from $name") }) {
            Text(text = name)
        }
    }
}
