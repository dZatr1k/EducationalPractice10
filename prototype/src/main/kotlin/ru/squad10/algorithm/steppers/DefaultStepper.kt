package ru.squad10.algorithm.steppers

import ru.squad10.algorithm.GraphProcessorRunner
import ru.squad10.algorithm.StepSize

class DefaultStepper(private val graphProcessorRunner: GraphProcessorRunner) : Stepper {
    override fun start() {
        while (!graphProcessorRunner.isFinishedReadonly.get()) {
            graphProcessorRunner.step(StepSize.BIG)
        }
    }

    override fun stop() {}
}