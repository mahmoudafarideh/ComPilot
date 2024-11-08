package m.a.compilot.compiler.builder

import com.google.devtools.ksp.symbol.KSClassDeclaration

interface ComposableBuilder {
    fun generateComposableFunction(
        classDeclaration: KSClassDeclaration,
        navigationBuilder: NavigationBuilder
    ): String
}