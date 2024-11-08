package m.a.compilot.compiler.builder

import com.google.devtools.ksp.symbol.KSClassDeclaration
import m.a.compilot.compiler.builder.extension.ArgumentsFunctionGenerator
import m.a.compilot.compiler.builder.extension.BundleParserFunctionGenerator
import m.a.compilot.compiler.builder.extension.CompositeFunctionGenerator
import m.a.compilot.compiler.builder.extension.NavigatorFunctionGenerator
import m.a.compilot.compiler.builder.extension.RouteFunctionGenerator

interface ExtensionFunctionGenerator {
    fun generate(
        navigationBuilder: NavigationBuilder,
        classDeclaration: KSClassDeclaration
    ): String

    companion object {
        fun generators() = CompositeFunctionGenerator(
            listOf(
                RouteFunctionGenerator(),
                ArgumentsFunctionGenerator(),
                NavigatorFunctionGenerator(),
                BundleParserFunctionGenerator()
            )
        )
    }
}