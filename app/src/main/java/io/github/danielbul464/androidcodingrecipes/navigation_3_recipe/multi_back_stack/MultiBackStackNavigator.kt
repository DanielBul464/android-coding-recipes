package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.multi_back_stack

import androidx.compose.runtime.snapshots.Snapshot
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.Route
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.TopLevelRoute

/** Manages navigation across multiple independent back stacks. */
class MultiBackStackNavigator(
    val state: MultiBackStackNavigationState,
) {

    fun navigate(
        route: Route,
        backStackPolicy: BackStackPolicy = BackStackPolicy.Preserve,
    ) {
        if (route is TopLevelRoute) {
            val targetBackStack = findBackStack(route) ?: return
            Snapshot.withMutableSnapshot {
                navigateToTopLevel(route, targetBackStack, backStackPolicy)
            }
        } else {
            state.currentStack.add(route)
        }
    }

    fun navigate(
        route: Route,
        topLevelRoute: TopLevelRoute,
        backStackPolicy: BackStackPolicy = BackStackPolicy.Preserve,
    ) {
        if (route is TopLevelRoute) return
        val targetBackStack = findBackStack(topLevelRoute) ?: return

        Snapshot.withMutableSnapshot {
            navigateToTopLevel(topLevelRoute, targetBackStack, backStackPolicy)
            targetBackStack.add(route)
        }
    }

    fun replace(route: Route) {
        if (route is TopLevelRoute) {
            if (findBackStack(route) == null) return
            state.topLevelRoute = route
            return
        }

        val currentStack = state.currentStack
        Snapshot.withMutableSnapshot {
            if (currentStack.size > 1) {
                currentStack.removeLastOrNull()
            }
            currentStack.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.currentStack
        if (currentStack.size > 1) {
            currentStack.removeLastOrNull()
        } else if (state.topLevelRoute::class != state.startRoute::class) {
            state.topLevelRoute = state.startRoute
        }
    }

    private fun navigateToTopLevel(
        route: TopLevelRoute,
        targetBackStack: NavBackStack<NavKey>,
        backStackPolicy: BackStackPolicy,
    ) {
        when (backStackPolicy) {
            BackStackPolicy.Preserve -> Unit
            BackStackPolicy.PopToRoot -> targetBackStack.popToRoot()
            BackStackPolicy.Recreate -> targetBackStack.recreateStack(route)
        }
        state.topLevelRoute = route
    }

    private fun findBackStack(route: TopLevelRoute): NavBackStack<NavKey>? =
        state.backStacks[route::class]
}

/** Defines how a target top-level back stack is handled during navigation. */
enum class BackStackPolicy {
    Preserve,
    PopToRoot,
    Recreate,
}

private fun NavBackStack<NavKey>.popToRoot() {
    while (size > 1) {
        removeLastOrNull()
    }
}

private fun NavBackStack<NavKey>.recreateStack(route: TopLevelRoute) {
    clear()
    add(route)
}
