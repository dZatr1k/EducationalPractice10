package ru.squad10

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.stage.Stage

class AlgoApp : Application() {
    companion object {
        lateinit var stage: Stage
            private set
    }

    override fun start(primaryStage: Stage) {
        stage = primaryStage

        val representation = GraphRepresentation()

        val splitPane = SplitPane(
            representation.matrixPane,
            representation.graphPane
        )
        val scene = Scene(splitPane)
        primaryStage.scene = scene
        primaryStage.title = "prototype"
        primaryStage.height = 500.0
        primaryStage.width = 1000.0
        primaryStage.show()

        representation.graphPane.init()

        representation.readonlyGraphProperty.addListener { _, _, value ->
            println(value)
        }
    }
}

fun main(vararg args: String) {
    Application.launch(AlgoApp::class.java, *args)
}
