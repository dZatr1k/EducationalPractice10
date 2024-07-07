package ru.squad10.log.visual

import javafx.scene.control.Label
import javafx.scene.layout.VBox

class VisualLoggerPane : VBox() {
//    private val vbox = VBox()

    init {
//        children += vbox
        style =
            """
                -fx-font-family: "Consolas";
            """.trimIndent()
    }

    fun addLog(message: String) {
        children.add(Label(message))
    }
}
