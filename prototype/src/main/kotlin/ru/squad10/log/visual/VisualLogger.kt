package ru.squad10.log.visual

import ru.squad10.log.Logger

class VisualLogger(val pane: VisualLoggerPane) : Logger() {
    override fun log(message: String) {
        pane.addLog(message)
    }
}