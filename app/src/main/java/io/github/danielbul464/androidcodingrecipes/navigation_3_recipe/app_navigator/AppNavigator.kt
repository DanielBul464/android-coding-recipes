package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.app_navigator

import androidx.compose.runtime.snapshots.Snapshot
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.Route

class AppNavigator(
    private val mutableBackStack: NavBackStack<NavKey>,
) {

    val backStack: List<NavKey> = mutableBackStack

    fun navigate(route: Route) {
        mutableBackStack.add(route)
    }

    fun replace(route: Route) {
        Snapshot.withMutableSnapshot {
            mutableBackStack.removeLastOrNull()
            mutableBackStack.add(route)
        }
    }

    fun newRootScreen(route: Route) {
        Snapshot.withMutableSnapshot {
            mutableBackStack.clear()
            mutableBackStack.add(route)
        }
    }

    fun goBack() {
        if (mutableBackStack.size > 1) {
            mutableBackStack.removeLastOrNull()
        }
    }
}
