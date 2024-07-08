package ru.squad10.log

class StdioLogger : Logger() {
    override fun log(level: Level, message: String) {
        println("[$level] $message")
    }
}