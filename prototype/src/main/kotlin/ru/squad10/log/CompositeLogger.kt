package ru.squad10.log

class CompositeLogger(private val loggers: Iterable<Logger>) : Logger() {
    override fun log(message: String) {
        loggers.forEach { it.log(message) }
    }
}