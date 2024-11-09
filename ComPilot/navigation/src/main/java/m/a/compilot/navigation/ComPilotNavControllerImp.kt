package m.a.compilot.navigation

import androidx.navigation.NavController
import m.a.compilot.common.RouteNavigator
import m.a.compilot.navigation.options.NavigationOptionsBuilder
import m.a.compilot.navigation.options.NavigationOptionsBuilderImp
import m.a.compilot.navigation.result.Navigationhandler

data class ComPilotNavControllerImp(
    override val navController: NavController,
    val currentDestinationId: Int?,
    val navOptionsBuilder: NavigationOptionsBuilder.() -> Unit = { }
) : ComPilotNavController {

    private val navigationOptionsBuilder = NavigationOptionsBuilderImp().apply {
        navOptionsBuilder()
    }

    override fun setResult(
        key: String,
        result: Navigationhandler.() -> Unit
    ): PopBackstackController {
        navigationOptionsBuilder.setResult(key, result)
        return this
    }

    override fun safePopBackStack() {
        if (navController.currentDestination?.id == currentDestinationId) {
            setResult()
            navController.popBackStack()
        }
    }

    override fun navigate(navigator: RouteNavigator) {
        val route = navigator.navigator()
        if (
            navigationOptionsBuilder.shouldNavigate(
                navigator.route(),
                navController.currentDestination,
                currentDestinationId
            )
        ) {
            setResult()
            navController.navigate(route) {
                val navigate = this
                with(navigationOptionsBuilder) { navigate.applyNavOptions() }
            }
        }
    }

    private fun setResult() {
        with(navigationOptionsBuilder) {
            navController.previousBackStackEntry?.applyResults()
        }
    }

    override fun safeNavigate(): NavigationController {
        navigationOptionsBuilder.safeNavigate()
        return this
    }

    override fun popToDestination(route: String) {
        navController.popBackStack(route, false)
    }

    override fun safePopToDestination(route: String) {
        if (navController.currentDestination?.id == currentDestinationId) {
            navController.popBackStack(route, false)
        }
    }

    override fun clearBackStack(): NavigationController {
        navigationOptionsBuilder.clearBackStack()
        return this
    }

    override fun checkShouldNavigate(): NavigationController {
        navigationOptionsBuilder.checkShouldNavigate()
        return this
    }

    override fun checkNotInRoutes(vararg route: String): NavigationController {
        navigationOptionsBuilder.checkNotInRoute(*route)
        return this
    }
}