// TARGET_BACKEND: WASM
// USE_JS_TAG

fun throwSomeJsException(): Int = js("{ throw new Error('Test'); }")
fun throwSomeJsPrimitive(): Int = js("{ throw null; }")

fun jsExceptionWithFinally(): Boolean {
    try {
        throwSomeJsException()
        return false
    } finally {
        return true
    }
    return false
}

fun jsExceptionWithThrowable(): Boolean {
    try {
        throwSomeJsException()
        return false
    } catch (e: Throwable) {
        val stacktrace = e.stackTraceToString()
        return stacktrace.contains("throwSomeJsException") &&
                stacktrace.contains("<main>.jsExceptionWithThrowable") &&
                stacktrace.contains("<main>.box")
    }
    return false
}

fun jsExceptionWithJsException(): Boolean {
    try {
        throwSomeJsException()
        return false
    } catch (e: JsException) {
        val stacktrace = e.stackTraceToString()
        return stacktrace.contains("throwSomeJsException") &&
                stacktrace.contains("<main>.jsExceptionWithJsException") &&
                stacktrace.contains("<main>.box")
    }
    return false
}

fun jsPrimitiveWithFinally(): Boolean {
    try {
        throwSomeJsPrimitive()
        return false
    } finally {
        return true
    }
    return false
}

fun jsPrimitiveWithThrowable(): Boolean {
    try {
        throwSomeJsPrimitive()
        return false
    } catch (e: Throwable) {
        return e.message == "Exception was thrown while running JavaScript code"
    }
    return false
}

fun jsPrimitiveWithJsException(): Boolean {
    try {
        throwSomeJsPrimitive()
        return false
    } catch (e: JsException) {
        return e.message == "Exception was thrown while running JavaScript code"
    }
    return false
}

fun box(): String {
    if (!jsExceptionWithFinally()) return "FAIL1"
    if (!jsExceptionWithThrowable()) return "FAIL2"
    if (!jsExceptionWithJsException()) return "FAIL3"

    if (!jsPrimitiveWithFinally()) return "FAIL4"
    if (!jsPrimitiveWithThrowable()) return "FAIL5"
    if (!jsPrimitiveWithJsException()) return "FAIL6"

    return "OK"
}
