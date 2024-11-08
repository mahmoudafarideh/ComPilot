package m.a.compilot.navigation

import androidx.navigation.NavController

/**
 * Interface representing a ComPilot navigation controller.
 * This interface provides methods for safe navigation between destinations in a composable navigation graph.
 */
interface ComPilotNavController : PopBackstackController, NavigationController {
    val navController: NavController
}
