package ru.squad10.algorithm.steppers

import ru.squad10.AlgoApp
import ru.squad10.algorithm.GraphProcessorRunner
import ru.squad10.algorithm.StepSize
import ru.squad10.log.Logger
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class AutoStepper(
    private val duration: Duration,
    private val graphProcessorRunner: GraphProcessorRunner
) {
    fun start() {
        var scheduled: ScheduledFuture<*>? = null
        scheduled = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
            {
                try {
                    AlgoApp.logger.log(Logger.Level.INFO, "Совершён малый шаг в автоматическом режиме")
                    graphProcessorRunner.step(StepSize.SMALL)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                if (graphProcessorRunner.isFinishedReadonly.get()) {
                    scheduled!!.cancel(false)
                }
            },
            duration.toMillis(),
            duration.toMillis(),
            TimeUnit.MILLISECONDS
        )
    }
}