package ru.squad10.dto

data class Edge (
    val from: Vertex,
    val to: Vertex
) {
    override fun toString(): String {
        return "$from->$to"
    }
}
