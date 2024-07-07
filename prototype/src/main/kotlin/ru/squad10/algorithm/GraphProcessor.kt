package ru.squad10.algorithm

import javafx.application.Platform
import javafx.beans.property.ReadOnlyObjectWrapper
import kotlinx.coroutines.*
import ru.squad10.algorithm.conditions.Condition
import ru.squad10.algorithm.conditions.DefaultCondition
import ru.squad10.dto.Edge
import ru.squad10.dto.Graph

class GraphProcessor {
    private var continueCondition: Condition = DefaultCondition()
    private var inker: UIInker? = null

    fun setInker(newInker: UIInker){
        inker = newInker
    }

    fun setContinueCondition(newCondition: Condition){
        continueCondition = newCondition
    }

    suspend fun processWarshell(graphProperty: ReadOnlyObjectWrapper<Graph>) {
        val graph = graphProperty.get()
        val vertices = graph.vertices.toList()
        val n = vertices.size
        val vertexIndex = vertices.withIndex().associate { it.value to it.index }
        val adjacencyMatrix = Array(n) { BooleanArray(n) }

        for (edge in graph.edges) {
            val fromIndex = vertexIndex[edge.from] ?: -1
            val toIndex = vertexIndex[edge.to] ?: -1
            adjacencyMatrix[fromIndex][toIndex] = true
        }

        for (k in 0 until n) {
            for (i in 0 until n) {
                for (j in 0 until n) {
                    if(i == k || j == k)
                        continue

                    if (adjacencyMatrix[i][k] && adjacencyMatrix[k][j]) {
                        val newEdge = Edge(vertices[i], vertices[j])
                        graphProperty.set(
                            Graph(
                                graphProperty.get().vertices,
                                graphProperty.get().edges + newEdge)
                        )

                        Platform.runLater {
                            inker?.colorNewEdge(newEdge, null, null)
                            inker?.colorCheckBox(i, j)
                        }
                    }
                    inker?.colorVertexes(vertices[k], vertices[i], vertices[j])
                    continueCondition.start()
                    waitForCondition()
                    inker?.resetStyleVertexes(vertices[k], vertices[i], vertices[j])
                }
            }
        }
    }

    private suspend fun waitForCondition(): Unit = coroutineScope {
        while (!continueCondition.getValue()) {
            delay(100)
        }
    }
}