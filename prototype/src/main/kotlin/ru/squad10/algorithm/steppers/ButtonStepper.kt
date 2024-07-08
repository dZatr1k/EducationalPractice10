package ru.squad10.algorithm.steppers

import javafx.scene.control.Button
import ru.squad10.AlgoApp
import ru.squad10.algorithm.GraphProcessorRunner
import ru.squad10.algorithm.StepSize
import ru.squad10.log.Logger

class ButtonStepper (
    private val smallButton: Button,
    private val mediumButton: Button,
    private val bigButton: Button,
    private val graphProcessorRunner: GraphProcessorRunner
) : Stepper{
    private fun step(stepSize: StepSize){
        try {
            graphProcessorRunner.step(stepSize)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
    override fun start() {
        smallButton.setOnMouseClicked {
            AlgoApp.logger.log(Logger.Level.INFO, "Совершён малый шаг в ручном режиме")
            step(StepSize.SMALL)
        }

        mediumButton.setOnMouseClicked {
            AlgoApp.logger.log(Logger.Level.INFO, "Совершён средний шаг в ручном режиме")
            step(StepSize.MEDIUM)
        }

        bigButton.setOnMouseClicked {
            AlgoApp.logger.log(Logger.Level.INFO, "Совершён большой шаг в ручном режиме")
            step(StepSize.BIG)
        }

        graphProcessorRunner.isFinishedReadonly.addListener {_, _, value ->
            if(value)
                stop()
        }
    }

    override fun stop() {
        smallButton.setOnMouseClicked {  }
        mediumButton.setOnMouseClicked {  }
        bigButton.setOnMouseClicked {  }
    }
}