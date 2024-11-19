package m.a.compilot.compiler

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import m.a.compilot.common.RouteNavigation
import m.a.compilot.common.RouteType
import m.a.compilot.compiler.builder.ExtensionFunctionGenerator
import m.a.compilot.compiler.utils.addLine
import m.a.compilot.compiler.utils.toComposableBuilder
import m.a.compilot.compiler.utils.toNavigationBuilder
import m.a.compilot.compiler.utils.write
import m.a.compilot.compiler.utils.writeLines
import java.io.OutputStream

class NavigationVisitor(
    private val file: OutputStream
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val screenType = getScreenValue(classDeclaration) ?: return
        val navigationBuilder = classDeclaration.toNavigationBuilder() ?: return
        val packageName = "${classDeclaration.qualifiedName?.asString()}"
        file.addLine()
        appendImports(packageName)
        file.addLine()
        ExtensionFunctionGenerator.generators().generate(
            navigationBuilder, classDeclaration
        ).let {
            file.write(it)
        }

        screenType.toComposableBuilder().generateComposableFunction(
            classDeclaration, navigationBuilder
        ).let {
            file.write(it)
        }
        file.addLine()
        addNavigatorObject(
            file,
            classDeclaration.simpleName.asString(),
            navigationBuilder.shouldAddBundleParser()
        )
    }

    private fun appendImports(packageName: String) {
        file.writeLines(
            "import $packageName",
            "import android.os.Bundle",
            "import androidx.compose.animation.AnimatedContentScope",
            "import androidx.compose.animation.AnimatedContentTransitionScope",
            "import androidx.compose.animation.EnterTransition",
            "import androidx.compose.animation.ExitTransition",
            "import androidx.compose.runtime.Composable",
            "import androidx.compose.runtime.NonRestartableComposable",
            "import androidx.compose.runtime.remember",
            "import androidx.core.os.bundleOf",
            "import androidx.navigation.NamedNavArgument",
            "import androidx.navigation.NavBackStackEntry",
            "import androidx.navigation.NavDeepLink",
            "import androidx.navigation.NavGraphBuilder",
            "import androidx.navigation.compose.composable",
            "import androidx.navigation.NavType",
            "import androidx.navigation.navArgument",
            "import androidx.compose.ui.window.DialogProperties",
            "import com.google.accompanist.navigation.material.bottomSheet",
            "import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
            "import androidx.compose.foundation.layout.ColumnScope",
            "import androidx.navigation.compose.dialog",
            "import m.a.compilot.runtime.RouteStack",
            "import m.a.compilot.common.RouteNavigator",
        )
    }

    private fun addNavigatorObject(
        file: OutputStream,
        argumentType: String,
        shouldAddBundleParser: Boolean,
    ) {
        file.write(
            "val $argumentType.navigator: RouteNavigator\n" +
                    "    get() = object : RouteNavigator {\n" +
                    "        val navigator = this@navigator\n" +
                    "        override fun navigator(): String {\n" +
                    "            return navigator.navigator()\n" +
                    "        }\n" +
                    "\n" +
                    "        override fun route(): String = ${if (shouldAddBundleParser) argumentType else "navigator"}.navigationRoute()\n" +
                    "    }"
        )
    }

    private fun getScreenValue(classDeclaration: KSClassDeclaration): RouteType? {
        return classDeclaration.annotations.firstOrNull {
            it.shortName.asString() == RouteNavigation::class.java.simpleName
        }?.let {
            it.arguments.firstOrNull { argument ->
                argument.name?.asString() == "type"
            }?.let { argument ->
                RouteType.valueOf(argument.value.toString().split(".").last())
            }
        }
    }


}
