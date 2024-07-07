package ru.squad10.log

abstract class Logger {
    fun log(vararg message: Any) {
        log(message.joinToString(" ") { it.toString() })
    }

    protected abstract fun log(message: String)
}