package m.a.compilot.compiler.builder.extension

import com.google.devtools.ksp.symbol.KSClassDeclaration
import m.a.compilot.compiler.builder.ExtensionFunctionGenerator
import m.a.compilot.compiler.builder.NavigationBuilder

class BundleParserFunctionGenerator : ExtensionFunctionGenerator {
    override fun generate(
        navigationBuilder: NavigationBuilder,
        classDeclaration: KSClassDeclaration
    ): String {
        return buildString {
            if (navigationBuilder.shouldAddBundleParser()) {
                append("private fun Bundle.to${classDeclaration.simpleName.asString()}(): ${classDeclaration.simpleName.asString()} {\n")
                append("        ")
                append(
                    "return ${navigationBuilder.buildNavigationArgument(classDeclaration)}\n"
                )
                append("}\n")
            }
            append("@Composable\n@NonRestartableComposable\n")
            append("${navigationBuilder.objectExtensionPrefix(classDeclaration)}.rememberArguments(bundle: Bundle): ${classDeclaration.simpleName.asString()} {\n")
            append("    return remember {\n        ")
            if (navigationBuilder.shouldAddBundleParser()) {
                append("bundle.to${classDeclaration.simpleName.asString()}()")
            } else {
                append(navigationBuilder.buildNavigationArgument(classDeclaration))
            }
            append("\n    }\n")
            append("}")
        }
    }
}