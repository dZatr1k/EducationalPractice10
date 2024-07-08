package ru.squad10

import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import ru.squad10.algorithm.GraphProcessor
import ru.squad10.algorithm.LaunchType
import ru.squad10.algorithm.StepSize
import ru.squad10.algorithm.UIInker
import ru.squad10.algorithm.steppers.AutoStepper
import ru.squad10.algorithm.steppers.ButtonStepper
import ru.squad10.dto.Graph
import java.time.Duration

class GraphRepresentation {
    private val graphProperty = ReadOnlyObjectWrapper(Graph(setOf(), setOf()))
    val readonlyGraphProperty: ObservableValue<Graph> = graphProperty

    val matrixPane = MatrixIOPane(this, graphProperty)
    val graphPane = GraphVisPane(readonlyGraphProperty)
    private val graphProcessor = GraphProcessor(UIInker(matrixPane, graphPane), graphProperty, matrixPane.getCheckboxes())

    fun applyAlgorithm(launchType: LaunchType) {
        matrixPane.lockUI()
        val graphProcessorRunner = graphProcessor.newRunner()

        // TODO lock interface or smth while graph is processing
        graphProcessorRunner.isFinishedReadonly.addListener { _, _, value ->
            if (value) {
                graphProcessor.clearColor()
                matrixPane.unlockUI()
                println("Graph processing finished")
            }
        }

        when(launchType) {
            LaunchType.DEFAULT -> {
                while (!graphProcessorRunner.isFinishedReadonly.get()) {
                    graphProcessorRunner.step(StepSize.BIG)
                }
            }
            LaunchType.AUTO -> {
                AutoStepper(Duration.ofMillis(matrixPane.getAutomaticStepDelayInMills()), graphProcessorRunner).start()
            }
            LaunchType.MANUAL -> {
                ButtonStepper(matrixPane.smallStepButton, graphProcessorRunner).start()
            }
        }
    }

}