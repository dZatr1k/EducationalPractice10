package ru.squad10

import javafx.application.Application
import javafx.geometry.Orientation
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.stage.Stage
import ru.squad10.log.CompositeLogger
import ru.squad10.log.Logger
import ru.squad10.log.MetadataLoggerDecorator
import ru.squad10.log.StdioLogger
import ru.squad10.log.visual.VisualLogger
import ru.squad10.log.visual.VisualLoggerPane

class AlgoApp : Application() {
    companion object {
        lateinit var stage: Stage
            private set
        lateinit var logger: Logger
            private set
    }

    override fun start(primaryStage: Stage) {
        stage = primaryStage
        val pane = VisualLoggerPane()
        logger = MetadataLoggerDecorator(CompositeLogger(listOf(
            StdioLogger(),
            VisualLogger(pane)
        )))

        val representation = GraphRepresentation()
        stage.setOnCloseRequest {
            representation.stop()
        }

        val splitPane = SplitPane(
            representation.matrixPane,
            representation.graphPane
        )

        val mainSplitPane = SplitPane(
            splitPane,
            pane
        )
        mainSplitPane.orientation = Orientation.VERTICAL

        val scene = Scene(mainSplitPane)
        primaryStage.scene = scene
        primaryStage.title = "Worshall's algorithm"
        primaryStage.height = 500.0
        primaryStage.width = 1000.0
        primaryStage.show()

        representation.graphPane.init()

        representation.readonlyGraphProperty.addListener { _, _, value ->
            println(value)
        }
    }
}
