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

        val matrixIOPane = MatrixIOPane()
        val graphVisPane = GraphVisPane(matrixIOPane.readonlyGraphProperty)

        val splitPane = SplitPane(
            matrixIOPane,
            graphVisPane
        )

        val mainSplitPane = SplitPane(
            splitPane,
            pane
        )
        mainSplitPane.orientation = Orientation.VERTICAL

        val scene = Scene(mainSplitPane)
        primaryStage.scene = scene
        primaryStage.title = "prototype"
        primaryStage.height = 500.0
        primaryStage.width = 1000.0
        primaryStage.show()

        graphVisPane.init()
    }
}
