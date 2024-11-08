package m.a.compilot.navigation

import m.a.compilot.common.RouteNavigator

interface NavigationController {

    /**
     * Clears the back stack of the navigation controller.
     */
    fun clearBackStack(): NavigationController

    /**
     * Navigates to the specified destination only if it is called from current compose nav graph.
     */
    fun safeNavigate(): NavigationController

    /**
     * Checks if navigation should occur based on the current route.
     * If the navigation route equals to the current route it won't navigate.
     */
    fun checkShouldNavigate(): NavigationController

    /**
     * Checks if the current route is not within the specified navigation routes.
     *
     * @param route The [String] to check against.
     */
    fun checkNotInRoutes(vararg route: String): NavigationController

    /**
     * Navigates to the specified [RouteNavigator].
     *
     * @param navigator The [RouteNavigator] to navigate to.
     */
    fun navigate(navigator: RouteNavigator)
}