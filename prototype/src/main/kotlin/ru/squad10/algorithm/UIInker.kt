package ru.squad10.algorithm

import ru.squad10.GraphVisPane
import ru.squad10.MatrixIOPane
import ru.squad10.dto.Edge
import ru.squad10.dto.Vertex

class UIInker(private val matrixPane: MatrixIOPane, private val graphPane: GraphVisPane) {
    fun colorNewEdge(newEdge: Edge?, firstFormativeEdge: Edge?, secondFormativeEdge: Edge?){
        if (newEdge != null) {
            graphPane.setEdgeColor(newEdge, "green")
        }

        if (firstFormativeEdge != null) {
            graphPane.setEdgeColor(firstFormativeEdge, "blue")
        }

        if (secondFormativeEdge != null) {
            graphPane.setEdgeColor(secondFormativeEdge, "blue")
        }
    }

    fun colorCheckBox(fromIndex: Int, toIndex: Int){
        matrixPane.setCheckboxColor(fromIndex, toIndex, "green")
    }

    fun colorVertexes(firstVertex: Vertex?, secondVertex: Vertex?, thirdVertex: Vertex?){
        if (firstVertex != null) {
            graphPane.setVertexColor(firstVertex, "blue")
        }
        if (secondVertex != null) {
            graphPane.setVertexColor(secondVertex, "yellow")
        }
        if (thirdVertex != null) {
            graphPane.setVertexColor(thirdVertex, "yellow")
        }
    }

    fun resetStyleVertexes(firstVertex: Vertex?, secondVertex: Vertex?, thirdVertex: Vertex?){
        if (firstVertex != null) {
            graphPane.resetVertexStyle(firstVertex)
        }
        if (secondVertex != null) {
            graphPane.resetVertexStyle(secondVertex)
        }
        if (thirdVertex != null) {
            graphPane.resetVertexStyle(thirdVertex)
        }
    }
}