package m.a.compilot.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import m.a.compilot.common.RouteNavigation
import m.a.compilot.compiler.utils.writeLine

class ComposeNavigationRouteProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotationName = RouteNavigation::class.qualifiedName ?: return listOf()
        val symbols = resolver
            .getSymbolsWithAnnotation(annotationName)
            .filterIsInstance<KSClassDeclaration>()

        if (!symbols.iterator().hasNext()) return emptyList()

        val files = resolver.getAllFiles().toList()

        symbols.forEach {
            val packageName = it.packageName.asString()
            val file = codeGenerator.createNewFile(
                dependencies = Dependencies(false, *files.toTypedArray()),
                packageName = it.packageName.asString(),
                fileName = "${it.simpleName.asString()}NavigationRoutes"
            )
            file.writeLine("package $packageName.routes")
            it.accept(NavigationVisitor(file, logger), Unit)
            file.close()
        }

        return symbols.filterNot { it.validate() }.toList()
    }
}