package m.a.compilot.compiler.builder

import com.google.devtools.ksp.symbol.KSClassDeclaration

interface NavigationBuilder {
    fun buildRoute(classDeclaration: KSClassDeclaration): String
    fun buildArguments(classDeclaration: KSClassDeclaration): List<String>
    fun buildNavigator(classDeclaration: KSClassDeclaration): String
    fun buildNavigationArgument(classDeclaration: KSClassDeclaration): String
    fun objectExtensionPrefix(classDeclaration: KSClassDeclaration): String
    fun shouldAddBundleParser(): Boolean

    companion object {
        const val ROUTE_DELIMITER = "/"
    }
}