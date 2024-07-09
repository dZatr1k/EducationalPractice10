package ru.squad10.algorithm.steppers

import javafx.application.Platform
import ru.squad10.AlgoApp
import ru.squad10.algorithm.GraphProcessorRunner
import ru.squad10.algorithm.StepSize
import ru.squad10.log.Logger
import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class AutoStepper(
    private val duration: Duration,
    private val graphProcessorRunner: GraphProcessorRunner
) : Stepper{

    private var executor: ScheduledExecutorService? = null
    private var scheduled: ScheduledFuture<*>? = null

    override fun start() {
        executor = Executors.newSingleThreadScheduledExecutor()
        scheduled = executor!!.scheduleAtFixedRate(
            {
                try {
                    Platform.runLater{
                        AlgoApp.logger.log(Logger.Level.INFO, "Совершён малый шаг в автоматическом режиме")
                    }
                    graphProcessorRunner.step(StepSize.SMALL)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                if (graphProcessorRunner.isFinishedReadonly.get()) {
                    stop()
                }
            },
            duration.toMillis(),
            duration.toMillis(),
            TimeUnit.MILLISECONDS
        )
    }

    override fun stop() {
        scheduled!!.cancel(false)
        executor!!.shutdownNow()
    }
}