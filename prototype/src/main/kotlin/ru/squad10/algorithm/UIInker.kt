package ru.squad10.algorithm

import ru.squad10.GraphVisPane
import ru.squad10.MatrixIOPane
import ru.squad10.dto.Edge
import ru.squad10.dto.Vertex

class UIInker(private val matrixPane: MatrixIOPane, private val graphPane: GraphVisPane) {
    private val coloredVertexes: MutableSet<Vertex> = mutableSetOf()
    private val coloredEdges: MutableSet<Edge> = mutableSetOf()
    private val coloredNewEdges: MutableSet<Edge> = mutableSetOf()

    private fun colorNewEdges(){
        for(edge in coloredNewEdges){
            graphPane.setEdgeColor(edge, "green")
        }
    }

    fun colorNewEdge(newEdge: Edge?, firstFormativeEdge: Edge?, secondFormativeEdge: Edge?){
        resetStyleForOldEdges()
        colorNewEdges()

        if (newEdge != null) {
            graphPane.setEdgeColor(newEdge, "green")
            coloredEdges.add(newEdge)
            coloredNewEdges.add(newEdge)
        }

        if (firstFormativeEdge != null) {
            graphPane.setEdgeColor(firstFormativeEdge, "blue")
            coloredEdges.add(firstFormativeEdge)
        }

        if (secondFormativeEdge != null) {
            graphPane.setEdgeColor(secondFormativeEdge, "blue")
            coloredNewEdges.add(secondFormativeEdge)
        }
    }

    fun colorCheckBox(fromIndex: Int, toIndex: Int){
        matrixPane.setCheckboxColor(fromIndex, toIndex, "green")
    }

    fun colorVertexes(firstVertex: Vertex?, secondVertex: Vertex?, thirdVertex: Vertex?){
        resetStyleForOldVertexes()

        if (firstVertex != null) {
            graphPane.setVertexColor(firstVertex, "blue")
            coloredVertexes.add(firstVertex)
        }
        if (secondVertex != null) {
            graphPane.setVertexColor(secondVertex, "yellow")
            coloredVertexes.add(secondVertex)
        }
        if (thirdVertex != null) {
            graphPane.setVertexColor(thirdVertex, "yellow")
            coloredVertexes.add(thirdVertex)
        }
    }

    fun resetStyleForOldVertexes(){
        for(vertex in coloredVertexes){
            graphPane.resetVertexStyle(vertex)
        }
        coloredVertexes.clear()
    }

    fun resetStyleForOldEdges(){
        for(edge in coloredEdges){
            graphPane.resetEdgeStyle(edge)
        }
        coloredEdges.clear()
    }
}