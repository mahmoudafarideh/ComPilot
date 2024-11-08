package m.a.compilot.compiler.builder.extension

import com.google.devtools.ksp.symbol.KSClassDeclaration
import m.a.compilot.compiler.builder.ExtensionFunctionGenerator
import m.a.compilot.compiler.builder.NavigationBuilder

class CompositeFunctionGenerator(
    private val generators: List<ExtensionFunctionGenerator>
) : ExtensionFunctionGenerator {
    override fun generate(
        navigationBuilder: NavigationBuilder,
        classDeclaration: KSClassDeclaration
    ): String {
        return buildString {
            generators.forEach {
                append(it.generate(navigationBuilder, classDeclaration))
                append("\n\n")
            }
        }
    }
}