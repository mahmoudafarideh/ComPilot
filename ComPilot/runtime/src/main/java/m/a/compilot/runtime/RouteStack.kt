package m.a.compilot.runtime

import androidx.navigation.NavBackStackEntry

data class RouteStack<T>(
    val navBackStackEntry: NavBackStackEntry,
    val argument: T
)