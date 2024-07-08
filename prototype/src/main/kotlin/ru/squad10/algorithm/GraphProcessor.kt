package ru.squad10.algorithm

import javafx.application.Platform
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.scene.control.CheckBox
import ru.squad10.AlgoApp
import ru.squad10.dto.Edge
import ru.squad10.dto.Graph
import ru.squad10.log.Logger

class GraphProcessor(
    private val inker: UIInker,
    private val graphProperty: ReadOnlyObjectWrapper<Graph>,
    private val checkboxes: MutableMap<Pair<Int, Int>, CheckBox> = mutableMapOf(),
) {
    fun newRunner(): GraphProcessorRunner {
        inker.resetStyleNewEdges()
        inker.resetStyleNewCheckboxes()

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
                        val firstFormativeEdge = Edge(vertices[i], vertices[k])
                        val secondFormativeEdge = Edge(vertices[k], vertices[j])

                        inker.resetStyleForOldEdges()
                        inker.resetStyleForOldVertexes()
                        inker.resetLablesStyle()
                        inker.resetCheckboxesStyle()
                        inker.colorVertexes(vertices[k], vertices[i], vertices[j])
                        inker.colorMainLabels(k+1, k+1)
                        inker.colorFormativeLabels(i+1, j+1)
                        inker.colorFormativeCheckBoxes(i, k, k, j)
                        Platform.runLater{
                            AlgoApp.logger.log(Logger.Level.INFO, "Рассматриваются вершины ${vertices[i]}, ${vertices[k]}, ${vertices[j]} и рёбра ${vertices[i]}->${vertices[k]}, ${vertices[k]}->${vertices[j]}")
                        }
                        if (adjacencyMatrix[i][k] && adjacencyMatrix[k][j]) {
                            Platform.runLater{
                                inker.colorFormativeEdges(firstFormativeEdge, secondFormativeEdge)
                            }
                            val newEdge = Edge(vertices[i], vertices[j])
                            if(graphProperty.get().edges.contains(newEdge))
                                return@GraphProcessingStep
                            adjacencyMatrix[i][j] = true
                            graphProperty.set(
                                Graph(
                                    graphProperty.get().vertices,
                                    graphProperty.get().edges + newEdge
                                )
                            )
                            Platform.runLater {
                                inker.colorNewEdge(newEdge)
                            }
                            checkboxes[i to j]!!.isSelected = true
                            inker.colorNewCheckBox(i, j)
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

    fun clearColor(){
        inker.resetCheckboxesStyle()
        inker.resetStyleForOldVertexes()
        inker.resetLablesStyle()
        Platform.runLater {
            inker.resetStyleForOldEdges()
        }
    }
}