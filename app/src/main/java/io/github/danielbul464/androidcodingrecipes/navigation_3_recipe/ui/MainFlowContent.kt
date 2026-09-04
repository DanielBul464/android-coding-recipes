package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.TopLevelRoute
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.multi_back_stack.rememberMultiBackStackNavigationState
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.multi_back_stack.rememberMultiBackStackNavigator

@Composable
fun MainFlowContent(modifier: Modifier = Modifier) {
    val topLevelRoutes = remember {
        setOf<TopLevelRoute>(
            MainFlowDestination.Home1,
            MainFlowDestination.Catalog1,
            MainFlowDestination.Login1,
            MainFlowDestination.Profile1,
        )
    }
    val navigationState = rememberMultiBackStackNavigationState(
        startRoute = MainFlowDestination.Home1,
        topLevelRoutes = topLevelRoutes,
    )
    val navigator = rememberMultiBackStackNavigator(navigationState)
    val entryProvider = remember { mainFlowEntryProvider() }
    val entries = navigationState.toDecoratedEntries(
        multiBackStackNavigator = navigator,
        entryProvider = entryProvider,
    )

    NavDisplay(
        entries = entries,
        modifier = modifier.fillMaxSize(),
        onBack = navigator::goBack,
    )
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
            .background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick = { TODO("Navigate from $name") }) {
            Text(text = name)
        }
    }
}
