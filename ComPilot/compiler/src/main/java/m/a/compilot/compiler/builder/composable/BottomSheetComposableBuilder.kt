package m.a.compilot.compiler.builder.composable

import com.google.devtools.ksp.symbol.KSClassDeclaration
import m.a.compilot.compiler.builder.ComposableBuilder
import m.a.compilot.compiler.builder.NavigationBuilder

class BottomSheetComposableBuilder : ComposableBuilder {
    override fun generateComposableFunction(
        classDeclaration: KSClassDeclaration,
        navigationBuilder: NavigationBuilder
    ): String {
        val argumentType = classDeclaration.simpleName.asString()
        return "${navigationBuilder.objectExtensionPrefix(classDeclaration)}.bottomSheet(\n" +
                "    navGraphBuilder: NavGraphBuilder,\n" +
                "    deepLinks: List<NavDeepLink> = emptyList(),\n" +
                "    content: @Composable ColumnScope.(RouteStack<$argumentType>) -> Unit\n" +
                ") {\n" +
                "    navGraphBuilder.bottomSheet(\n" +
                "        route = this.navigationRoute(),\n" +
                "        arguments = this.arguments(),\n" +
                "        deepLinks = deepLinks,\n" +
                "        content = {\n" +
                "            val argument = rememberArguments(it.arguments ?: bundleOf())\n" +
                "            content(RouteStack(it, argument))\n" +
                "        }\n" +
                "    )\n" +
                "}\n"
    }
}