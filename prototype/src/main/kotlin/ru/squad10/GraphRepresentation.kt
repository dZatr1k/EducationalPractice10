package ru.squad10

import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import ru.squad10.algorithm.GraphProcessor
import ru.squad10.algorithm.LaunchType
import ru.squad10.algorithm.UIInker
import ru.squad10.algorithm.steppers.AutoStepper
import ru.squad10.dto.Graph
import java.time.Duration

class GraphRepresentation {
    private val graphProperty = ReadOnlyObjectWrapper(Graph(setOf(), setOf()))
    val readonlyGraphProperty: ObservableValue<Graph> = graphProperty

    val matrixPane = MatrixIOPane(this, graphProperty)
    val graphPane = GraphVisPane(readonlyGraphProperty)
    private val graphProcessor = GraphProcessor(UIInker(matrixPane, graphPane), graphProperty)

    fun applyAlgorithm(launchType: LaunchType) {
        val graphProcessorRunner = graphProcessor.newRunner()

        // TODO lock interface or smth while graph is processing
        graphProcessorRunner.isFinishedReadonly.addListener { _, _, value ->
            if (value) {
                println("Graph processing finished")
            }
        }

        when(launchType) {
            LaunchType.DEFAULT -> TODO()
            LaunchType.AUTO -> {
                AutoStepper(Duration.ofMillis(250), graphProcessorRunner).start()
            }
        }
    }

}