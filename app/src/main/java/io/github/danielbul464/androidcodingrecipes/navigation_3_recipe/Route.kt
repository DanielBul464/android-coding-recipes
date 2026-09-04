package io.github.danielbul464.androidcodingrecipes.navigation_3_recipe

import androidx.navigation3.runtime.NavKey

/**
 * Marks a destination that can be stored in a Navigation 3 back stack.
 *
 * Implementations should be serializable so the back stack can be restored after configuration
 * changes and process recreation.
 */
interface Route : NavKey
