package m.a.compilot.compiler.builder.extension

import com.google.devtools.ksp.symbol.KSClassDeclaration
import m.a.compilot.compiler.builder.ExtensionFunctionGenerator
import m.a.compilot.compiler.builder.NavigationBuilder

class NavigatorFunctionGenerator : ExtensionFunctionGenerator {
    override fun generate(
        navigationBuilder: NavigationBuilder,
        classDeclaration: KSClassDeclaration
    ): String {
        return buildString {
            append("fun ${classDeclaration.simpleName.asString()}.navigator(): String {\n")
            append("    return")
            append(" ${navigationBuilder.buildNavigator(classDeclaration)}\n")
            append("}")
        }
    }
}