package io.github.danielbul464.androidcodingrecipes.exo_player.core.domain

/**
 * A handle for a scheduled action.
 */
interface VideoPlayerScheduledAction {

    /** Cancels the scheduled action if it has not run yet. */
    fun cancel()
}
