package ru.squad10.algorithm

import ru.squad10.GraphVisPane
import ru.squad10.MatrixIOPane
import ru.squad10.dto.Edge
import ru.squad10.dto.Vertex

class UIInker(private val matrixPane: MatrixIOPane, private val graphPane: GraphVisPane) {
    private val coloredVertexes: MutableSet<Vertex> = mutableSetOf()
    private val coloredEdges: MutableSet<Edge> = mutableSetOf()
    private val coloredNewEdges: MutableSet<Edge> = mutableSetOf()
    private val coloredLabels: MutableSet<Pair<Int, Int>> = mutableSetOf()
    private val coloredNewCheckboxes: MutableSet<Pair<Int, Int>> = mutableSetOf()
    private val coloredCheckboxes: MutableSet<Pair<Int, Int>> = mutableSetOf()

    private fun colorNewEdges(){
        for(edge in coloredNewEdges){
            graphPane.setEdgeColor(edge, "green")
        }
    }

    fun resetStyleNewCheckboxes(){
        try{
            for(pair in coloredNewCheckboxes){
                matrixPane.resetCheckboxStyle(pair.first, pair.second)
            }
        }
        finally {
            coloredNewCheckboxes.clear()
        }
    }

    fun resetStyleNewEdges(){
        try{
            for(edge in coloredNewEdges){
                graphPane.resetEdgeStyle(edge)
            }
        }
        finally {
            coloredNewEdges.clear()
        }
    }

    fun resetLablesStyle(){
        for(pair in coloredLabels){
            matrixPane.resetGridLabelStyle(pair.first, pair.second)
        }
        coloredLabels.clear()
    }

    fun resetCheckboxesStyle(){
        for(pair in coloredCheckboxes){
            matrixPane.resetCheckboxStyle(pair.first, pair.second)
        }
        coloredCheckboxes.clear()
        colorNewCheckboxes()
    }

    fun colorNewCheckboxes(){
        for(pair in coloredNewCheckboxes){
            matrixPane.setCheckboxColor(pair.first, pair.second, "green")
        }
    }

    fun colorFormativeEdges(firstFormativeEdge: Edge?, secondFormativeEdge: Edge?) {
        if (firstFormativeEdge != null) {
            graphPane.setEdgeColor(firstFormativeEdge, "blue")
            coloredEdges.add(firstFormativeEdge)
        }

        if (secondFormativeEdge != null) {
            graphPane.setEdgeColor(secondFormativeEdge, "blue")
            coloredEdges.add(secondFormativeEdge)
        }
    }

    fun colorNewEdge(newEdge: Edge?){
        if (newEdge != null) {
            graphPane.setEdgeColor(newEdge, "green")
            coloredEdges.add(newEdge)
            coloredNewEdges.add(newEdge)
        }
    }

    fun colorMainLabels(fromIndex: Int, toIndex: Int){
        matrixPane.setGridLabelColor(0, fromIndex, "blue")
        matrixPane.setGridLabelColor(toIndex, 0, "blue")
        coloredLabels.add(0 to fromIndex)
        coloredLabels.add(toIndex to 0)
    }

    fun colorFormativeLabels(fromIndex: Int, toIndex: Int){
        matrixPane.setGridLabelColor(0, fromIndex, "red")
        matrixPane.setGridLabelColor(toIndex, 0, "red")
        coloredLabels.add(0 to fromIndex)
        coloredLabels.add(toIndex to 0)
    }

    fun colorNewCheckBox(fromIndex: Int, toIndex: Int){
        matrixPane.setCheckboxColor(fromIndex, toIndex, "green")
        coloredCheckboxes.add(fromIndex to toIndex)
        coloredNewCheckboxes.add(fromIndex to toIndex)
    }

    fun colorFormativeCheckBoxes(firstFromIndex: Int, firstToIndex: Int, secondFromIndex: Int, secondToIndex: Int){
        matrixPane.setCheckboxColor(firstFromIndex, firstToIndex, "blue")
        coloredCheckboxes.add(firstFromIndex to firstToIndex)
        matrixPane.setCheckboxColor(secondFromIndex, secondToIndex, "blue")
        coloredCheckboxes.add(secondFromIndex to secondToIndex)
    }

    fun colorVertexes(firstVertex: Vertex?, secondVertex: Vertex?, thirdVertex: Vertex?){
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
        colorNewEdges()
    }
}