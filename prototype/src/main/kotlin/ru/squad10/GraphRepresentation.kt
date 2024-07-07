package ru.squad10

import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.squad10.dto.Graph
import ru.squad10.algorithm.GraphProcessor
import ru.squad10.algorithm.LaunchType
import ru.squad10.algorithm.UIInker
import ru.squad10.algorithm.conditions.AutoCondition
import ru.squad10.algorithm.conditions.DefaultCondition

class GraphRepresentation {
    private val graphProperty = ReadOnlyObjectWrapper(Graph(setOf(), setOf()))
    val readonlyGraphProperty: ObservableValue<Graph> = graphProperty

    private val graphProcessor = GraphProcessor()
    val matrixPane = MatrixIOPane(this, graphProperty)
    val graphPane = GraphVisPane(readonlyGraphProperty)

    @OptIn(DelicateCoroutinesApi::class)
    fun applyAlgorithm(launchType: LaunchType = LaunchType.DEFAULT) {
        GlobalScope.launch{
            graphProcessor.setContinueCondition(
                when(launchType){
                    LaunchType.DEFAULT -> DefaultCondition()
                    LaunchType.AUTO -> AutoCondition()
                }
            )
            graphProcessor.processWarshell(graphProperty)
        }
    }

    init {
        graphProcessor.setInker(UIInker(matrixPane, graphPane))
    }
}