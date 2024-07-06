package ru.squad10

import com.brunomnsilva.smartgraph.graph.DigraphEdgeList
import com.brunomnsilva.smartgraph.graphview.ForceDirectedSpringGravityLayoutStrategy
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.scene.layout.AnchorPane
import ru.squad10.dto.Edge
import ru.squad10.dto.Graph
import ru.squad10.dto.Vertex

class GraphVisPane(private val graphObservable: ObservableValue<Graph>) : AnchorPane() {
    private val diGraph = DigraphEdgeList<Vertex, Edge>()
    private val graphView = SmartGraphPanel(diGraph, ForceDirectedSpringGravityLayoutStrategy())

    val vertexCache = mutableMapOf<Vertex, com.brunomnsilva.smartgraph.graph.Vertex<Vertex>>()
    val edgeCache = mutableMapOf<Edge, com.brunomnsilva.smartgraph.graph.Edge<Edge, Vertex>>()

    private fun onGraphUpdated(value: ObservableValue<out Graph>, prev: Graph, cur: Graph) {
        val addedVertices = cur.vertices - prev.vertices
        val removedVertices = prev.vertices - cur.vertices
        val addedEdges = cur.edges - prev.edges
        val removedEdges = prev.edges - cur.edges

        for (addedVertex in addedVertices) {
            vertexCache[addedVertex] = diGraph.insertVertex(addedVertex)
        }
        for (removedVertex in removedVertices) {
            diGraph.removeVertex(vertexCache[removedVertex])
            vertexCache.remove(removedVertex)
        }
        for (addedEdge in addedEdges) {
            edgeCache[addedEdge] = diGraph.insertEdge(addedEdge.from, addedEdge.to, addedEdge)
        }
        for (removedEdge in removedEdges) {
            diGraph.removeEdge(edgeCache[removedEdge])
            edgeCache.remove(removedEdge)
        }

        resetEdgesStyle()
        graphView.update()
    }

    fun resetEdgesStyle(){
        for (edge in diGraph.edges()) {
            graphView.getStylableEdge(edge)?.setStyleInline(null)
        }
    }

    fun resetEdgeStyle(edge: Edge) {
        graphView.getStylableEdge(edge).setStyleInline(null)
    }

    fun setEdgeColor(edge: Edge, color: String) {
        graphView.getStylableEdge(edge).setStyleInline("-fx-stroke: $color;")
    }

    fun showNewEdges(oldGraph: Graph, newGraph: Graph){
        for (edge in newGraph.edges) {
            if(oldGraph.edges.contains(edge))
                continue
            graphView.getStylableEdge(diGraph.edges().first { diEdge -> diEdge.element() == edge }.element()).setStyleInline("-fx-stroke: green;")
        }
    }

    fun init() {
        graphView.init()
        onGraphUpdated(graphObservable, Graph(setOf(), setOf()), graphObservable.value)
    }

    init {
        val properties = SmartGraphProperties()
//        val container = SmartGraphDemoContainer(graphView)

        graphObservable.addListener(::onGraphUpdated)

        graphView.setAutomaticLayout(true)
        children.add(graphView)

        setLeftAnchor(graphView, 0.0)
        setRightAnchor(graphView, 0.0)
        setTopAnchor(graphView, 0.0)
        setBottomAnchor(graphView, 0.0)
    }
}
