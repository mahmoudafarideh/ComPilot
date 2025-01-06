package m.a.compilot.compiler

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class ComposeNavigationRouteProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ComposeNavigationRouteProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}