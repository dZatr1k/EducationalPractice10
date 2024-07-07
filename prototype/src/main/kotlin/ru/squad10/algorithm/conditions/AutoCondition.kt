package ru.squad10.algorithm.conditions

import javafx.animation.PauseTransition
import javafx.util.Duration

class AutoCondition(private val durationInSeconds: Double = 1.0) : Condition {
    private var pause: PauseTransition = PauseTransition(Duration.seconds(durationInSeconds))
    private var condition: Boolean = false

    override fun start() {
        condition = false
        pause.playFromStart()
    }

    override fun getValue(): Boolean {
        return condition
    }

    init {
        pause.setOnFinished { condition = true }
    }
}