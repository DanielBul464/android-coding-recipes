package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.app_navigator.rememberAppNavigator
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.app_navigator.rememberAppNavigatorNavEntryDecorator

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(AppDestination.CustomSplash)
    val appNavigator = rememberAppNavigator(backStack)

    NavDisplay(
        backStack = appNavigator.backStack,
        modifier = modifier.fillMaxSize(),
        onBack = appNavigator::goBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberAppNavigatorNavEntryDecorator(appNavigator),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<AppDestination.CustomSplash> {
                AppFlowScreen(
                    buttonText = "Open onboarding",
                    onButtonClick = { appNavigator.newRootScreen(AppDestination.Onboarding) },
                )
            }

            entry<AppDestination.Onboarding> {
                AppFlowScreen(
                    buttonText = "Open main flow",
                    onButtonClick = { appNavigator.newRootScreen(AppDestination.MainFlow) },
                )
            }

            entry<AppDestination.MainFlow> {
                MainFlowContent()
            }
        },
    )
}

@Composable
private fun AppFlowScreen(
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick = onButtonClick) {
            Text(text = buttonText)
        }
    }
}
