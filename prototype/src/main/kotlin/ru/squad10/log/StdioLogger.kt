package ru.squad10.log

class StdioLogger : Logger() {
    override fun log(message: String) {
        println(message)
    }
}