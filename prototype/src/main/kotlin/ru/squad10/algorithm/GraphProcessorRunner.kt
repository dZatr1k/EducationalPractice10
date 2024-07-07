package ru.squad10.algorithm

import javafx.beans.property.ReadOnlyBooleanWrapper

class GraphProcessorRunner(
    steps: Iterable<GraphProcessingStep>
) {
    private val smallSteps: Map<Int, Map<Int, Map<Int, List<GraphProcessingStep>>>> =
        steps
            .groupBy { it.indices[StepSize.BIG]!! }
            .mapValues {
                it.value
                    .groupBy {
                        it.indices[StepSize.MEDIUM]!!
                    }
            }
            .mapValues {
                it.value.mapValues {
                    it.value.groupBy {
                        it.indices[StepSize.SMALL]!!
                    }
                }
            }

    private val indices = mutableMapOf(
        StepSize.BIG to 0,
        StepSize.MEDIUM to 0,
        StepSize.SMALL to 0,
    )

    private val isFinished = ReadOnlyBooleanWrapper(false)
    val isFinishedReadonly = isFinished.readOnlyProperty

    private fun tryIncrementBigStep() {
        if (smallSteps[indices[StepSize.BIG]!! + 1] == null) {
            println("[big] Finished")
            isFinished.set(true)
        } else {
            indices[StepSize.BIG] = indices[StepSize.BIG]!! + 1
            indices[StepSize.MEDIUM] = 0
            indices[StepSize.SMALL] = 0
            println("[big] New indices: $indices")
        }
    }

    private fun tryIncrementMediumStep() {
        if (smallSteps[indices[StepSize.BIG]]!![indices[StepSize.MEDIUM]!! + 1] == null) {
            println("[medium] Trying to increment big step")
            tryIncrementBigStep()
        } else {
            indices[StepSize.MEDIUM] = indices[StepSize.MEDIUM]!! + 1
            indices[StepSize.SMALL] = 0
            println("[medium] New indices: $indices")
        }
    }

    private fun tryIncrementSmallStep() {
        if (smallSteps[indices[StepSize.BIG]]!![indices[StepSize.MEDIUM]!!]!![indices[StepSize.SMALL]!! + 1] == null) {
            println("[small] Trying to increment medium step")
            tryIncrementMediumStep()
        } else {
            indices[StepSize.SMALL] = indices[StepSize.SMALL]!! + 1
            println("[small] New indices: $indices")
        }
    }

    fun step(size: StepSize) {
        println("Step: $size $indices")
        when (size) {
            StepSize.BIG -> {
                smallSteps[indices[StepSize.BIG]]!!
                    .filter { it.key >= indices[StepSize.MEDIUM]!! }
                    .flatMap { it.value.values }
                    .flatten()
                    .forEach { it.action.run() }
                tryIncrementBigStep()
            }

            StepSize.MEDIUM -> {
                smallSteps[indices[StepSize.BIG]]!![indices[StepSize.MEDIUM]]!!
                    .filter { it.key >= indices[StepSize.SMALL]!! }
                    .flatMap { it.value }
                    .forEach { it.action.run() }
                tryIncrementMediumStep()
            }

            StepSize.SMALL -> {
                smallSteps[indices[StepSize.BIG]]!![indices[StepSize.MEDIUM]]!![indices[StepSize.SMALL]]!!.forEach {
                    it.action.run()
                }
                tryIncrementSmallStep()
            }
        }
        println("Step finished")
    }
}