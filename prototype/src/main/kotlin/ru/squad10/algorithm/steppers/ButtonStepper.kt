package ru.squad10.algorithm.steppers

import javafx.scene.control.Button
import ru.squad10.algorithm.GraphProcessorRunner
import ru.squad10.algorithm.StepSize

class ButtonStepper(
    private val button: Button,
    private val graphProcessorRunner: GraphProcessorRunner
)  {
    fun start() {
        button.setOnMouseClicked {
            try {
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