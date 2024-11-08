package m.a.compilot

import m.a.compilot.common.RouteNavigation
import m.a.compilot.common.RouteType

@RouteNavigation
data class FullScreenRoute(
    val title: String
) {
    companion object
}

@RouteNavigation(type = RouteType.Dialog)
data class DialogRoute(
    val id: Int
) {
    companion object
}

@RouteNavigation(type = RouteType.BottomSheet)
data class BottomSheetRoute(
    val label: String
) {
    companion object
}