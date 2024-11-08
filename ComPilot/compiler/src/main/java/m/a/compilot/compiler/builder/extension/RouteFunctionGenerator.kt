package m.a.compilot.compiler.builder.extension

import com.google.devtools.ksp.symbol.KSClassDeclaration
import m.a.compilot.compiler.builder.ExtensionFunctionGenerator
import m.a.compilot.compiler.builder.NavigationBuilder

class RouteFunctionGenerator : ExtensionFunctionGenerator {
    override fun generate(
        navigationBuilder: NavigationBuilder,
        classDeclaration: KSClassDeclaration
    ): String {
        return buildString {
            append(
                "${navigationBuilder.objectExtensionPrefix(classDeclaration)}.navigationRoute(): String {\n"
            )
            append("    return ")
            append("\"${navigationBuilder.buildRoute(classDeclaration)}\"\n")
            append("}")
        }
    }
}