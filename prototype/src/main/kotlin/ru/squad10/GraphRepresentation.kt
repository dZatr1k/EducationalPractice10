package ru.squad10

import javafx.application.Platform
import javafx.beans.property.BooleanProperty
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableValue
import ru.squad10.algorithm.*
import ru.squad10.algorithm.steppers.AutoStepper
import ru.squad10.algorithm.steppers.ButtonStepper
import ru.squad10.algorithm.steppers.DefaultStepper
import ru.squad10.algorithm.steppers.Stepper
import ru.squad10.dto.Graph
import ru.squad10.log.Logger
import java.time.Duration

class GraphRepresentation {
    private val graphProperty = ReadOnlyObjectWrapper(Graph(setOf(), setOf()))
    private var visualizationState = ReadOnlyObjectWrapper(LaunchType.DEFAULT)
    private var observableVisualizationState: ObservableValue<LaunchType> = visualizationState

    val readonlyGraphProperty: ObservableValue<Graph> = graphProperty

    private var isRunning = ReadOnlyObjectWrapper(false)
    private val observableIsRunning: ObservableValue<Boolean> = isRunning

    val matrixPane = MatrixIOPane(this, graphProperty, visualizationState)
    val graphPane = GraphVisPane(readonlyGraphProperty)

    private val UIInker = UIInker(matrixPane, graphPane)
    private val graphProcessor = GraphProcessor(UIInker, graphProperty, matrixPane.getCheckboxes())

    private var currentGraphProcessorRunner: GraphProcessorRunner? = null
    private var stepper: Stepper? = null

    fun applyAlgorithm() {
        UIInker.setLaunchType(visualizationState.get())
        currentGraphProcessorRunner = graphProcessor.newRunner()

        // TODO lock interface or smth while graph is processing
        currentGraphProcessorRunner!!.isFinishedReadonly.addListener { _, _, value ->
            if (value) {
                stop()
                Platform.runLater{
                    AlgoApp.logger.log(Logger.Level.INFO, "Алгоритм закончил свою работу")
                }
            }
        }

        isRunning.set(true)
        changeAlgorithmMode()
    }

    private fun changeAlgorithmMode(){
        when(visualizationState.get()) {
            LaunchType.DEFAULT -> {
                AlgoApp.logger.log(Logger.Level.INFO, "Алгоритм запустился в стандартном режиме")
                stepper = DefaultStepper(currentGraphProcessorRunner!!)
            }
            LaunchType.AUTO -> {
                AlgoApp.logger.log(Logger.Level.INFO, "Алгоритм запустился в автоматическом режиме визуализации")
                stepper = AutoStepper(Duration.ofMillis(matrixPane.getAutomaticStepDelayInMills()), currentGraphProcessorRunner!!)
            }
            LaunchType.MANUAL -> {
                AlgoApp.logger.log(Logger.Level.INFO, "Алгоритм запустился в ручном режиме визуализации")
                stepper = ButtonStepper(matrixPane.smallStepButton, matrixPane.mediumStepButton, matrixPane.bigStepButton, currentGraphProcessorRunner!!)
            }
        }
        stepper?.start()
    }

    fun isRunning() : ObservableValue<Boolean> = observableIsRunning

    fun stop(){
        if(stepper != null){
            stepper!!.stop()
            graphProcessor.clearColor()
            currentGraphProcessorRunner = null
            stepper = null
            isRunning.set(false)
        }
    }

    init{
        observableVisualizationState.addListener{ _, _, cur ->
            if(stepper != null){
                stepper!!.stop()
                changeAlgorithmMode()
            }
        }
    }
}