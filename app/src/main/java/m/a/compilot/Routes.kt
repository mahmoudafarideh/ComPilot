package m.a.compilot

import m.a.compilot.common.RouteNavigation
import m.a.compilot.common.RouteType

@RouteNavigation
data class FullScreenRoute(
    val title: String
) {
    companion object
}

@RouteNavigation
data class FullScreenWithNestedArgRoute(
    val nested: NestedData,
    val child: Child
) {
    data class NestedData(
        val id: Int,
        val name: String,
    )

    enum class Child {
        Child1,
        Child2
    }

    companion object
}

@RouteNavigation
data class FullScreenWithNestedNullableArgRoute(
    val nested: NestedData?,
    val nested2: NestedData,
) {
    data class NestedData(
        val test: Test?,
        val enum: EnumClass?,
        val name: String
    ) {
        data class Test(
            val tester: Long
        )

        enum class EnumClass {
            One,
            Two
        }
    }

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