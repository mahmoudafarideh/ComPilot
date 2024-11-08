package m.a.compilot.navigation

import m.a.compilot.navigation.result.NavigationResult

interface PopBackstackController {

    /**
     * Sets a result to be returned to the previous destination.
     *
     * @param key The key associated with the result.
     * @param result The result to be set, defined by a [NavigationResult] block.
     */
    fun setResult(key: String, result: NavigationResult.() -> Unit = {}): PopBackstackController

    /**
     * Safely pops the back stack if the current navigation is part of the composable navigation graph.
     */
    fun safePopBackStack()

    /**
     * Pops the back stack to the specified destination.
     *
     * @param route The route of the destination to pop to.
     */
    fun popToDestination(route: String)

    /**
     * Safely pops the back stack to the specified destination if required.
     *
     * @param route The route of the destination to pop to.
     */
    fun safePopToDestination(route: String)
}