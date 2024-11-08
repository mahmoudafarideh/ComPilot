package m.a.compilot.compiler.builder.composable

import com.google.devtools.ksp.symbol.KSClassDeclaration
import m.a.compilot.compiler.builder.ComposableBuilder
import m.a.compilot.compiler.builder.NavigationBuilder

class ScreenComposableBuilder : ComposableBuilder {
    override fun generateComposableFunction(
        classDeclaration: KSClassDeclaration,
        navigationBuilder: NavigationBuilder
    ): String {
        return "${navigationBuilder.objectExtensionPrefix(classDeclaration)}.screen(\n" +
            "    navGraphBuilder: NavGraphBuilder,\n" +
            "    deepLinks: List<NavDeepLink> = emptyList(),\n" +
            "    enterTransition: (@JvmSuppressWildcards\n" +
            "    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? = null,\n" +
            "    exitTransition: (@JvmSuppressWildcards\n" +
            "    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? = null,\n" +
            "    popEnterTransition: (@JvmSuppressWildcards\n" +
            "    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? =\n" +
            "        enterTransition,\n" +
            "    popExitTransition: (@JvmSuppressWildcards\n" +
            "    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? =\n" +
            "        exitTransition,\n" +
            "    content: @Composable AnimatedContentScope.(RouteStack<${classDeclaration.simpleName.asString()}>) -> Unit\n" +
            ") {\n" +
            "    navGraphBuilder.composable(\n" +
            "        route = this.navigationRoute(),\n" +
            "        arguments = this.arguments(),\n" +
            "        deepLinks = deepLinks,\n" +
            "        enterTransition = enterTransition,\n" +
            "        exitTransition = exitTransition,\n" +
            "        popEnterTransition = popEnterTransition,\n" +
            "        popExitTransition = popExitTransition,\n" +
            "        content = {\n" +
            "            val argument = rememberArguments(it.arguments ?: bundleOf())\n" +
            "            this.content(RouteStack(it, argument))\n" +
            "        }\n" +
            "    )\n" +
            "}\n"
    }
}