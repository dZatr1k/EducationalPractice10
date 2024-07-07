package ru.squad10.algorithm

import javafx.application.Platform
import javafx.beans.property.ReadOnlyObjectWrapper
import ru.squad10.dto.Edge
import ru.squad10.dto.Graph

class GraphProcessor(
    private val inker: UIInker,
    private val graphProperty: ReadOnlyObjectWrapper<Graph>
) {
    fun newRunner(): GraphProcessorRunner {
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

        val steps = mutableListOf<GraphProcessingStep>()

        var currentMediumIndex = 0
        var currentSmallIndex = 0

        for (k in 0 until n) {
            currentMediumIndex = 0
            for (i in 0 until n) {
                var utilizedMediumLoop = false
                currentSmallIndex = 0
                for (j in 0 until n) {
                    if (i == k || j == k)
                        continue

                    steps += GraphProcessingStep(
                        mapOf(
                            StepSize.BIG to k,
                            StepSize.MEDIUM to currentMediumIndex,
                            StepSize.SMALL to currentSmallIndex
                        )
                    ) {
                        Platform.runLater {
                            if (adjacencyMatrix[i][k] && adjacencyMatrix[k][j]) {
                                val newEdge = Edge(vertices[i], vertices[j])
                                graphProperty.set(
                                    Graph(
                                        graphProperty.get().vertices,
                                        graphProperty.get().edges + newEdge
                                    )
                                )

                                inker.colorNewEdge(newEdge, null, null)
                                inker.colorCheckBox(i, j)
                            }
                            inker.colorVertexes(vertices[k], vertices[i], vertices[j])
//                            inker.resetStyleVertexes(vertices[k], vertices[i], vertices[j])
                        }
                    }
                    utilizedMediumLoop = true
                    currentSmallIndex++
                }
                if (utilizedMediumLoop) {
                    currentMediumIndex++
                }
            }
        }

        return GraphProcessorRunner(steps)
    }

}