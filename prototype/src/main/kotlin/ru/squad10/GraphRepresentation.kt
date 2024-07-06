package ru.squad10

import javafx.application.Platform
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.squad10.dto.Graph
import ru.squad10.algorithm.GraphProcessor

class GraphRepresentation {
    private val graphProperty = ReadOnlyObjectWrapper(Graph(setOf(), setOf()))
    val readonlyGraphProperty: ObservableValue<Graph> = graphProperty

    private val graphProcessor = GraphProcessor()
    val matrixPane = MatrixIOPane(this, graphProperty)
    val graphPane = GraphVisPane(readonlyGraphProperty)

    @OptIn(DelicateCoroutinesApi::class)
    fun applyAlgorithmInstantly() {
        GlobalScope.launch{
            val oldGraph = graphProperty.get()
            val newGraph = graphProcessor.processWarshell(oldGraph)
            matrixPane.showNewEdges(newGraph)
            Platform.runLater {graphPane.showNewEdges(oldGraph, newGraph)}
        }
    }
}