package ru.squad10

import javafx.application.Platform
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import ru.squad10.dto.Edge
import ru.squad10.dto.Graph
import ru.squad10.services.GraphProcessor

class GraphRepresentation {
    private val graphProperty = ReadOnlyObjectWrapper(Graph(setOf(), setOf()))
    val readonlyGraphProperty: ObservableValue<Graph> = graphProperty

    private val graphProcessor = GraphProcessor()
    val matrixPane = MatrixIOPane(this, graphProperty)
    val graphPane = GraphVisPane(readonlyGraphProperty)

    fun startAlgorithm() {
        val oldGraph = graphProperty.get()
        val newGraph = graphProcessor.processWarshell(oldGraph)
        matrixPane.showNewEdges(newGraph)
        Platform.runLater {graphPane.showNewEdges(oldGraph, newGraph)}

    }
}