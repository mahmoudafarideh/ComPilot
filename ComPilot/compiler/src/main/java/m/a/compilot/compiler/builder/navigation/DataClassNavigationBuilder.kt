package m.a.compilot.compiler.builder.navigation

import com.google.devtools.ksp.closestClassDeclaration
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.Modifier
import m.a.compilot.compiler.builder.NavigationBuilder
import m.a.compilot.compiler.builder.NavigationBuilder.Companion.ROUTE_DELIMITER

class DataClassNavigationBuilder : NavigationBuilder {

    override fun buildRoute(classDeclaration: KSClassDeclaration): String {
        return buildRouteBuiltIn(classDeclaration)
    }

    private fun buildRouteBuiltIn(classDeclaration: KSClassDeclaration): String {
        val parameters = classDeclaration.getParameters()
        return buildString {
            append(
                (classDeclaration.packageName.asString() + "." + classDeclaration.simpleName.asString()).replace(
                    ".",
                    ROUTE_DELIMITER
                )
            )
            append(ROUTE_DELIMITER)
            appendParameters(parameters)
        }
    }

    override fun buildArguments(classDeclaration: KSClassDeclaration): List<String> {
        val parameters = classDeclaration.getParameters().flatMap { it.arguments() }
        return parameters.map {
            "navArgument(\"${it.first}\") { \n" +
                "            type = ${it.second.name.toNavType(it.third)}\n" +
                "            nullable = ${it.third}\n" +
                "        }"
        }
    }

    override fun buildNavigator(classDeclaration: KSClassDeclaration): String {
        var route = buildRouteBuiltIn(classDeclaration)
        val parameters = classDeclaration.getParameters().flatMap { it.parameters() }
        parameters.forEach { (name, _, isNullable) ->
            val replacer = if (!isNullable) {
                "\${this" + ".${name}}"
            } else {
                "\${this" + ".${name} ?: \"\"}"
            }
            route = route.replace("{${name}}", replacer)
        }
        return "\"$route\""
    }

    override fun buildNavigationArgument(classDeclaration: KSClassDeclaration): String {
        return buildString {
            append("${classDeclaration.simpleName.asString()}(\n")
            val parameters = classDeclaration.getParameters().map { it.getFromBundleString(null) }
            parameters.forEach {
                append(
                    "            $it,\n"
                )

            }
            append("        )")
        }
    }

    override fun objectExtensionPrefix(classDeclaration: KSClassDeclaration): String {
        return "fun ${classDeclaration.simpleName.asString()}.Companion"
    }

    override fun shouldAddBundleParser(): Boolean = true

    private fun String.toNavType(isNullable: Boolean) = when (this) {
        "Int" -> "NavType.IntType"
        "Float", "Double" -> "NavType.FloatType"
        "String" -> "NavType.StringType"
        "Boolean" -> "NavType.BoolType"
        "Long" -> "NavType.LongType"
        else -> {
            throw IllegalArgumentException("This Argument Type is not supported $this")
        }
    }.let {
        if (isNullable) "NavType.StringType"
        else it
    }

    private val KSTypeReference.classDeclaration get() = this.resolve().declaration.closestClassDeclaration()

    private fun StringBuilder.appendParameters(parameters: List<DataClassParameters>) {
        val parameterList = parameters.flatMap { it.parameters() }
        parameterList.filter { !it.second }.forEach { kParameter ->
            append("{${kParameter.first}}$ROUTE_DELIMITER")
        }
        parameterList.filter { it.second }.let { kParameterList ->
            if (kParameterList.isEmpty()) return@let
            append("?")
            kParameterList.forEachIndexed { index, kParameter ->
                append("${kParameter.first}={${kParameter.first}}")
                if (index < kParameterList.lastIndex) append("&")
            }
        }
    }

    private fun KSClassDeclaration.getParameters(): List<DataClassParameters> {
        val arguments = this.primaryConstructor?.parameters ?: return emptyList()
        return arguments.map { it.getParameter() }
    }

    private fun KSValueParameter.getParameter(): DataClassParameters {
        this.type.classDeclaration?.let {
            if (it.modifiers.contains(Modifier.DATA)) {
                return DataClassParameters.DataClassParameter(
                    name = name!!.asString(),
                    clazz = it.qualifiedName!!.asString(),
                    parameters = it.primaryConstructor!!.parameters.map { parameter ->
                        parameter.getParameter()
                    }
                )
            } else if (it.classKind == ClassKind.ENUM_CLASS) {
                return DataClassParameters.EnumParameter(
                    name = name!!.asString(),
                    clazz = it.qualifiedName!!.asString(),
                    isNullable = isNullable(),
                )
            }
        }
        val typeName = when (type.toString()) {
            "Int", "Float", "Double", "String", "Boolean", "Long" ->
                DataClassParameters.PrimitiveParameter.Type.valueOf(type.toString())

            else -> {
                throw IllegalArgumentException("This Argument Type is not supported $this")
            }
        }
        return DataClassParameters.PrimitiveParameter(
            name!!.asString(),
            typeName,
            isNullable(),
            receivesEmpty()
        )
    }

    private fun KSValueParameter.isNullable() = this.type.resolve().isMarkedNullable
    private fun KSValueParameter.receivesEmpty() =
        this.type.resolve().isMarkedNullable || this.type.toString() == "String"
}

sealed class DataClassParameters {

    abstract fun parameters(): List<Triple<String, Boolean, Boolean>>
    abstract fun arguments(): List<Triple<String, PrimitiveParameter.Type, Boolean>>
    abstract fun getFromBundleString(prefix: String? = null): String

    data class PrimitiveParameter(
        val name: String,
        val type: Type,
        val isNullable: Boolean,
        val receivesEmpty: Boolean
    ) : DataClassParameters() {

        enum class Type {
            Int, Float, Double, Long, Boolean, String;

            fun getFromBundle(name: kotlin.String, isNullable: kotlin.Boolean): kotlin.String {
                val nullableGetter =
                    if (isNullable) "getString(\"$name\", \"\").takeIf { it.isNotEmpty() }" else ""
                var result = when (this.toString()) {
                    "Int" -> if (isNullable) "$nullableGetter?.let { it.toInt() }" else "getInt(\"$name\")"
                    "Float" -> if (isNullable) "$nullableGetter?.let { it.toFloat() }" else "getFloat(\"$name\")"
                    "Double" -> if (isNullable) "$nullableGetter?.let { it.toDouble() }" else "getFloat(\"$name\").toDouble()"
                    "String" -> "getString(\"$name\", \"\")"
                    "Boolean" -> if (isNullable) "$nullableGetter?.let { it.toBoolean() }" else "getBoolean(\"$name\")"
                    "Long" -> if (isNullable) "$nullableGetter?.let { it.toLong() }" else "getLong(\"$name\")"
                    else -> {
                        throw IllegalArgumentException("This Argument Type is not supported $this")
                    }
                }
                if (isNullable) {
                    result = result.let {
                        "if(containsKey(\"$name\")) $it else null"
                    }
                }
                return result
            }
        }

        override fun parameters(): List<Triple<String, Boolean, Boolean>> {
            return listOf(Triple(name, receivesEmpty, isNullable))
        }

        override fun arguments(): List<Triple<String, Type, Boolean>> {
            return listOf(Triple(name, type, isNullable))
        }

        override fun getFromBundleString(prefix: String?): String {
            return "${this.name} = ${
                type.getFromBundle("${prefix?.let { "$it." } ?: ""}$name",
                    isNullable)
            }"
        }
    }

    data class EnumParameter(
        val name: String,
        val clazz: String,
        val isNullable: Boolean,
    ) : DataClassParameters() {

        override fun parameters(): List<Triple<String, Boolean, Boolean>> {
            return listOf(Triple(name, false, isNullable))
        }

        override fun getFromBundleString(prefix: String?): String {
            return if (!isNullable) {
                "$name = $clazz.valueOf(${
                    PrimitiveParameter.Type.String.getFromBundle(
                        "${prefix?.let { "$it." } ?: ""}$name", false
                    )
                })"
            } else {
                "$name = (${
                    PrimitiveParameter.Type.String.getFromBundle(
                        "${prefix?.let { "$it." } ?: ""}$name", false
                    )
                })?.let { $clazz.valueOf(it) }"
            }
        }

        override fun arguments(): List<Triple<String, PrimitiveParameter.Type, Boolean>> {
            return listOf(Triple(name, PrimitiveParameter.Type.String, isNullable))
        }
    }

    data class DataClassParameter(
        val name: String,
        val clazz: String,
        val parameters: List<DataClassParameters>
    ) : DataClassParameters() {
        override fun parameters(): List<Triple<String, Boolean, Boolean>> {
            return parameters.flatMap {
                it.parameters().map { (name, receivesEmpty, isNullable) ->
                    Triple(this.name + "." + name, receivesEmpty, isNullable)
                }
            }
        }

        override fun arguments(): List<Triple<String, PrimitiveParameter.Type, Boolean>> {
            return parameters.flatMap {
                it.arguments().map { (name, type, isNullable) ->
                    Triple(this.name + "." + name, type, isNullable)
                }
            }
        }

        override fun getFromBundleString(prefix: String?): String {
            val parametersBundle = parameters.map {
                it.getFromBundleString("${prefix?.let { s -> "$s." } ?: ""}$name")
            }
            return buildString {
                append("$name = $clazz(\n")
                parametersBundle.forEachIndexed { index, s ->
                    append("                $s")
                    if (index < parametersBundle.lastIndex) {
                        append(",")
                    }
                    append("\n")
                }
                append("            )")
            }
        }
    }
}
