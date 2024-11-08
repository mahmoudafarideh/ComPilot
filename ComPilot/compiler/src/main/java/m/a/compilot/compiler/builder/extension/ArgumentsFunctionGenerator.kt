package m.a.compilot.compiler.builder.extension

import com.google.devtools.ksp.symbol.KSClassDeclaration
import m.a.compilot.compiler.builder.ExtensionFunctionGenerator
import m.a.compilot.compiler.builder.NavigationBuilder

class ArgumentsFunctionGenerator : ExtensionFunctionGenerator {
    override fun generate(
        navigationBuilder: NavigationBuilder,
        classDeclaration: KSClassDeclaration
    ): String {
        return buildString {
            append("${navigationBuilder.objectExtensionPrefix(classDeclaration)}.arguments(): List<NamedNavArgument> {\n")
            append("    return listOf(\n")
            navigationBuilder.buildArguments(classDeclaration).forEach {
                append("        $it,\n")
            }
            append("    )\n")
            append("}\n")
        }
    }
}