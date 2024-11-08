package m.a.compilot.compiler.builder.navigation

import com.google.devtools.ksp.symbol.KSClassDeclaration
import m.a.compilot.compiler.builder.NavigationBuilder
import m.a.compilot.compiler.builder.NavigationBuilder.Companion.ROUTE_DELIMITER

class EnumNavigationBuilder : NavigationBuilder {
    override fun buildRoute(classDeclaration: KSClassDeclaration): String {
        return buildString {
            append(
                (classDeclaration.packageName.asString() + "." +
                    classDeclaration.simpleName.asString() + "." +
                    "\$name"
                    ).replace(".", ROUTE_DELIMITER)
            )
            append(ROUTE_DELIMITER)
        }
    }

    override fun buildArguments(classDeclaration: KSClassDeclaration): List<String> = listOf()

    override fun buildNavigator(classDeclaration: KSClassDeclaration): String =
        "\"${buildRoute(classDeclaration)}\""

    override fun buildNavigationArgument(classDeclaration: KSClassDeclaration): String {
        return "this"
    }

    override fun objectExtensionPrefix(classDeclaration: KSClassDeclaration): String {
        return "fun ${classDeclaration.simpleName.asString()}"
    }

    override fun shouldAddBundleParser(): Boolean = false
}