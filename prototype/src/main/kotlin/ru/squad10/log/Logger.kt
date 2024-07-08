package ru.squad10.log

abstract class Logger {
    enum class Level {
        DEBUG,
        INFO,
        ERROR
    }

    fun log(level: Level, vararg message: Any) {
        log(level, message.joinToString(" ") { it.toString() })
    }

    protected abstract fun log(level: Level, message: String)
}