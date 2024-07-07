package ru.squad10.dto

import ru.squad10.dto.Edge

data class Graph(
     val vertices: Set<Vertex>,
     val edges: Set<Edge>
)
