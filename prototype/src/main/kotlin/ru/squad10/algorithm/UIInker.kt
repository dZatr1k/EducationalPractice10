package ru.squad10.algorithm

import ru.squad10.GraphVisPane
import ru.squad10.MatrixIOPane
import ru.squad10.dto.Edge

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
}