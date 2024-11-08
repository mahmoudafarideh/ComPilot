package m.a.compilot.common

annotation class RouteNavigation(val type: RouteType = RouteType.Screen)

enum class RouteType {
    Screen,
    Dialog,
    BottomSheet
}

interface RouteNavigator {
    fun navigator(): String
    fun route(): String
}