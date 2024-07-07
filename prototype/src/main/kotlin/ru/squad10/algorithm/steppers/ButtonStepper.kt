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
            if (graphProcessorRunner.isFinishedReadonly.get()) {
                button.setOnMouseClicked {  }
                return@setOnMouseClicked
            }
            try {
                graphProcessorRunner.step(StepSize.SMALL)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}