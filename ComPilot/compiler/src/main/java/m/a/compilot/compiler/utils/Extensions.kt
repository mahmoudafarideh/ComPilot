package m.a.compilot.compiler.utils

import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import m.a.compilot.compiler.builder.composable.DialogComposableBuilder
import m.a.compilot.common.RouteType
import m.a.compilot.compiler.builder.ComposableBuilder
import m.a.compilot.compiler.builder.NavigationBuilder
import m.a.compilot.compiler.builder.composable.BottomSheetComposableBuilder
import m.a.compilot.compiler.builder.composable.ScreenComposableBuilder
import m.a.compilot.compiler.builder.navigation.DataClassNavigationBuilder
import m.a.compilot.compiler.builder.navigation.DataObjectNavigationBuilder
import m.a.compilot.compiler.builder.navigation.EnumNavigationBuilder

internal fun KSClassDeclaration.toNavigationBuilder(): NavigationBuilder? {
    val modifiers = this.modifiers.toList()
    if (this.classKind == ClassKind.OBJECT) return DataObjectNavigationBuilder()
    if (this.classKind == ClassKind.ENUM_CLASS) return EnumNavigationBuilder()
    if (modifiers.contains(Modifier.DATA)) return DataClassNavigationBuilder()
    return null
}

internal fun RouteType.toComposableBuilder(): ComposableBuilder {
    return when (this) {
        RouteType.Screen -> ScreenComposableBuilder()
        RouteType.Dialog -> DialogComposableBuilder()
        RouteType.BottomSheet -> BottomSheetComposableBuilder()
    }
}