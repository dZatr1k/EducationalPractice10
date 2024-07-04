import ru.squad10.dto.Vertex

data class Edge (
    val from: Vertex,
    val to: Vertex
) {
    override fun toString(): String {
        return "$from->$to"
    }
}
