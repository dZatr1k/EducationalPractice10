package ru.squad10.log.visual

import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.VBox
import ru.squad10.log.Logger

class VisualLoggerPane : ScrollPane() {
    private val vbox = VBox()

    init {
        content = vbox
        style =
            """
                -fx-font-family: "Consolas";
            """.trimIndent()
    }

    fun addLog(level: Logger.Level, message: String) {
        vbox.children.add(Label(message).apply {
            val color = when (level) {
                Logger.Level.DEBUG -> "gray"
                Logger.Level.INFO -> "blue"
                Logger.Level.ERROR -> "red"
            }
            style = """-fx-text-fill: $color"""
        })
    }
}
