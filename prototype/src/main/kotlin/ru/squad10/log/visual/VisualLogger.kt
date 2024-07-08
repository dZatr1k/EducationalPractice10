package ru.squad10.log.visual

import ru.squad10.log.Logger

class VisualLogger(private val pane: VisualLoggerPane) : Logger() {
    override fun log(level: Level, message: String) {
        pane.addLog(level, message)
    }
}