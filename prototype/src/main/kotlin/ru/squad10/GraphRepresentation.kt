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
import ru.squad10.log.Logger
import java.time.Duration

class GraphRepresentation {
    private val graphProperty = ReadOnlyObjectWrapper(Graph(setOf(), setOf()))
    val readonlyGraphProperty: ObservableValue<Graph> = graphProperty

    val matrixPane = MatrixIOPane(this, graphProperty)
    val graphPane = GraphVisPane(readonlyGraphProperty)
    private val UIInker = UIInker(matrixPane, graphPane)
    private val graphProcessor = GraphProcessor(UIInker, graphProperty, matrixPane.getCheckboxes())

    fun applyAlgorithm(launchType: LaunchType) {
        matrixPane.lockUI()
        UIInker.setLaunchType(launchType)
        val graphProcessorRunner = graphProcessor.newRunner()

        // TODO lock interface or smth while graph is processing
        graphProcessorRunner.isFinishedReadonly.addListener { _, _, value ->
            if (value) {
                graphProcessor.clearColor()
                matrixPane.unlockUI()
                //AlgoApp.logger.log(Logger.Level.INFO, "Алгоритм закончил свою работу")
            }
        }

        when(launchType) {
            LaunchType.DEFAULT -> {
                AlgoApp.logger.log(Logger.Level.INFO, "Алгоритм запустился в стандартном режиме")
                while (!graphProcessorRunner.isFinishedReadonly.get()) {
                    graphProcessorRunner.step(StepSize.BIG)
                }
            }
            LaunchType.AUTO -> {
                AlgoApp.logger.log(Logger.Level.INFO, "Алгоритм запустился в автоматическом режиме визуализации")
                AutoStepper(Duration.ofMillis(matrixPane.getAutomaticStepDelayInMills()), graphProcessorRunner).start()
            }
            LaunchType.MANUAL -> {
                AlgoApp.logger.log(Logger.Level.INFO, "Алгоритм запустился в ручном режиме визуализации")
                ButtonStepper(matrixPane.smallStepButton, graphProcessorRunner).start()
            }
        }
    }

}