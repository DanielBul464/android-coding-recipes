package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.multi_back_stack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import io.github.danielbul464.androidcodingrecipes.navigation_3_recipe.TopLevelRoute
import kotlin.reflect.KClass

/**
 * Creates navigation state with an independent back stack for each top-level route.
 *
 * Each route in [topLevelRoutes] must have a unique concrete class, and the concrete class of
 * [startRoute] must be present in the set.
 */
@Composable
fun rememberMultiBackStackNavigationState(
    startRoute: TopLevelRoute,
    topLevelRoutes: Set<TopLevelRoute>,
): MultiBackStackNavigationState {
    val routesByClass = topLevelRoutes.associateBy { route -> route::class }
    require(routesByClass.size == topLevelRoutes.size) {
        "Each top-level route must have a unique class."
    }
    val startRouteClass = startRoute::class
    val initialStartRoute = requireNotNull(routesByClass[startRouteClass]) {
        "The start route must be one of the top-level routes."
    }
    val startRouteClassName = startRouteClass.stableName
    val topLevelRouteClassNames = routesByClass.keys
        .map { routeClass -> routeClass.stableName }
        .sorted()

    val topLevelRouteState = rememberSerializable(
        startRouteClassName,
        topLevelRouteClassNames,
        serializer = MutableStateSerializer(NavKeySerializer()),
    ) {
        mutableStateOf<NavKey>(initialStartRoute)
    }

    val backStacks = routesByClass.mapValues { (routeClass, route) ->
        key(routeClass.stableName) {
            rememberNavBackStack(route)
        }
    }

    return remember(startRouteClassName, topLevelRouteClassNames) {
        MultiBackStackNavigationState(
            startRoute = initialStartRoute,
            topLevelRouteState = topLevelRouteState,
            backStacks = backStacks,
        )
    }
}

/** Holds the independent back stacks and the currently selected top-level route. */
class MultiBackStackNavigationState(
    startRoute: TopLevelRoute,
    private val topLevelRouteState: MutableState<NavKey>,
    val backStacks: Map<KClass<out TopLevelRoute>, NavBackStack<NavKey>>,
) {

    private val startRouteClass = startRoute::class

    val startRoute: TopLevelRoute
        get() = backStacks.getValue(startRouteClass).requireRoot(startRouteClass)

    var topLevelRoute: TopLevelRoute
        get() {
            val routeClass = selectedRouteClass
            return backStacks.getValue(routeClass).requireRoot(routeClass)
        }
        set(value) {
            val backStack = backStacks[value::class] ?: return
            topLevelRouteState.value = backStack.requireRoot(value::class)
        }

    private val selectedRouteClass: KClass<out TopLevelRoute>
        get() = (topLevelRouteState.value as? TopLevelRoute)
            ?.let { route -> route::class }
            ?.takeIf(backStacks::containsKey)
            ?: startRouteClass

    val currentStack: NavBackStack<NavKey>
        get() = backStacks.getValue(topLevelRoute::class)

    @Composable
    fun toDecoratedEntries(
        multiBackStackNavigator: MultiBackStackNavigator,
        entryProvider: (NavKey) -> NavEntry<NavKey>,
    ): List<NavEntry<NavKey>> {
        val decoratedEntries = backStacks.mapValues { (routeClass, stack) ->
            key(routeClass.stableName) {
                val decorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
                    rememberMultiBackStackNavEntryDecorator(multiBackStackNavigator),
                    rememberViewModelStoreNavEntryDecorator(),
                )
                rememberDecoratedNavEntries(
                    backStack = stack,
                    entryDecorators = decorators,
                    entryProvider = entryProvider,
                )
            }
        }

        return getTopLevelRoutesInUse()
            .flatMap { decoratedEntries.getValue(it) }
    }

    private fun getTopLevelRoutesInUse(): List<KClass<out TopLevelRoute>> =
        if (topLevelRoute::class == startRouteClass) {
            listOf(startRouteClass)
        } else {
            listOf(startRouteClass, topLevelRoute::class)
        }
}

private val KClass<out TopLevelRoute>.stableName: String
    get() = requireNotNull(qualifiedName) {
        "A top-level route class must have a qualified name."
    }

private fun NavBackStack<NavKey>.requireRoot(
    expectedClass: KClass<out TopLevelRoute>,
): TopLevelRoute {
    val root = firstOrNull()
    check(root is TopLevelRoute && root::class == expectedClass) {
        "Back stack for $expectedClass must start with a matching top-level route."
    }
    return root
}
