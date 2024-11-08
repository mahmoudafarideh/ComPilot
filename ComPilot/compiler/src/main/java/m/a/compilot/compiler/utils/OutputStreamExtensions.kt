package m.a.compilot.compiler.utils

import java.io.OutputStream

operator fun OutputStream.plusAssign(str: String) {
    this.write(str.toByteArray())
}

internal fun OutputStream.writeLine(str: String) {
    this.write((str).toByteArray())
    addLine()
}

internal fun OutputStream.write(str: String) {
    this.write((str).toByteArray())
}

internal fun OutputStream.writeLines(vararg lines: String) {
    lines.forEach {
        writeLine(it)
    }
}

internal fun OutputStream.addLine() {
    addLines(1)
}

internal fun OutputStream.addLines(count: Int) {
    val str = buildString {
        repeat(count) {
            append("\n")
        }
    }
    this.write(str.toByteArray())
}