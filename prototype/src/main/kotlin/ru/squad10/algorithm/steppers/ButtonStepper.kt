package ru.squad10.algorithm.steppers

import javafx.scene.control.Button
import ru.squad10.AlgoApp
import ru.squad10.algorithm.GraphProcessorRunner
import ru.squad10.algorithm.StepSize
import ru.squad10.log.Logger

class ButtonStepper(
    private val button: Button,
    private val graphProcessorRunner: GraphProcessorRunner
)  {
    fun start() {
        button.setOnMouseClicked {
            try {
                AlgoApp.logger.log(Logger.Level.INFO, "Совершён малый шаг в ручном режиме")
                graphProcessorRunner.step(StepSize.SMALL)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        graphProcessorRunner.isFinishedReadonly.addListener {_, _, value ->
            if(value)
                button.setOnMouseClicked {  }
        }
    }
}