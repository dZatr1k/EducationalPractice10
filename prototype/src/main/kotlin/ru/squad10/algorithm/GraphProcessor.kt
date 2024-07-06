package ru.squad10.algorithm

import kotlinx.coroutines.*
import ru.squad10.dto.Edge
import ru.squad10.dto.Graph

class GraphProcessor {
    private val continueCondition: () -> Boolean = {true}

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun processWarshell(graph: Graph): Graph {
        val asyncAction = GlobalScope.async {
            val vertices = graph.vertices.toList()
            val n = vertices.size
            val vertexIndex = vertices.withIndex().associate { it.value to it.index }
            val adjacencyMatrix = Array(n) { BooleanArray(n) }

            for (edge in graph.edges) {
                val fromIndex = vertexIndex[edge.from] ?: -1
                val toIndex = vertexIndex[edge.to] ?: -1
                adjacencyMatrix[fromIndex][toIndex] = true
            }

            val newTransitiveEdges = mutableSetOf<Edge>()
            for (k in 0 until n) {
                for (i in 0 until n) {
                    for (j in 0 until n) {
                        if (adjacencyMatrix[i][j] || (adjacencyMatrix[i][k] && adjacencyMatrix[k][j])){
                            newTransitiveEdges.add(Edge(vertices[i], vertices[j]))
                        }
                    }
                }
            }
            return@async Graph(vertices.toSet(), newTransitiveEdges)
        }

        return asyncAction.await()
    }

    suspend fun waitForCondition(): Unit = coroutineScope {
        while (!continueCondition()) {
            delay(100)
        }
    }
}