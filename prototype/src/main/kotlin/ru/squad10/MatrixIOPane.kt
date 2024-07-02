package ru.squad10

import Edge
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import ru.squad10.dto.Graph
import ru.squad10.dto.Vertex

class MatrixIOPane : AnchorPane() {
    private var dim = SimpleIntegerProperty(0)
    private val vbox = VBox()
    private val grid = GridPane()
    private val checkboxes = mutableMapOf<Pair<Int, Int>, CheckBox>()
    private val vertexCache = mutableMapOf<Int, Vertex>()

    private val graphProperty = ReadOnlyObjectWrapper(Graph(setOf(), setOf()))
    val readonlyGraphProperty: ObservableValue<Graph> = graphProperty

    private fun addCheckbox(cb: CheckBox, i: Int, j: Int) {
        grid.add(cb, j + 1, i + 1)
        checkboxes[(i to j)] = cb

        val edge = Edge(vertexCache[i]!!, vertexCache[j]!!)

        cb.selectedProperty().addListener { _, _, cur ->
            if (cur) {
                graphProperty.set(
                    Graph(
                        graphProperty.get().vertices,
                        graphProperty.get().edges + edge
                    )
                )
            } else {
                graphProperty.set(
                    Graph(
                        graphProperty.get().vertices,
                        graphProperty.get().edges - edge
                    )
                )
            }
        }
    }

    private fun addElement() {
        val name = ('A' + dim.get()).toString()
        vertexCache[dim.get()] = Vertex(name)

        dim.set(dim.get() + 1)

        grid.add(Label(name), 0, dim.get() - 1 + 1)
        grid.add(Label(name), dim.get() - 1 + 1, 0)

        val j = dim.get() - 1
        for (i in 0 until dim.get()) {
            val cb = CheckBox()
            addCheckbox(cb, i, j)
            if (i == j) {
                cb.isDisable = true
            } else {
                val cb2 = CheckBox()
                addCheckbox(cb2, j, i)
            }
        }

        graphProperty.set(
            Graph(
                graphProperty.get().vertices + Vertex(name),
                graphProperty.get().edges
            )
        )
    }

    private fun removeElement() {
        val name = ('A' + dim.get() - 1).toString()
        val vertex = Vertex(name)
        grid.children.removeIf { node ->
            val toRemove = ((GridPane.getRowIndex(node) == dim.get()) || (GridPane.getColumnIndex(node) == dim.get()))
            toRemove
        }

        graphProperty.set(
            Graph(
                graphProperty.get().vertices - vertex,
                graphProperty.get().edges.filterNot { it.to == vertex || it.from == vertex }.toSet()
            )
        )

        dim.set(dim.get() - 1)
    }

    init {
        val buttonAddElement = Button("Добавить")
        val buttonRemoveElement = Button("Удалить")
        val buttonLoadGraph = Button("Загрузка")
        val buttonGenerateGraph = Button("Рандом")
        val buttonCheckbox = CheckBox("Показ работы")
        val buttonStart = Button("Запуск")

        val toggleGroupVisMode = ToggleGroup()
        val toggleButtonVisModeManual = ToggleButton("Ручной")
        val toggleButtonVisModelAuto = ToggleButton("Автоматический")

        toggleButtonVisModeManual.isSelected = true

        toggleGroupVisMode.toggles.addAll(toggleButtonVisModeManual, toggleButtonVisModelAuto)

        val visModelAutoTimeSelector = Slider(0.25, 10.0, 1.0)

        val visModelAutoPane = VBox(
            Label("Время шага"),
            visModelAutoTimeSelector
        )

        val visModeManualPane = HBox(
            Button("Малый шаг"),
            Button("Средний шаг"),
            Button("Большой шаг"),
        )

        buttonRemoveElement.disableProperty().bind(dim.greaterThan(2).not())
        buttonAddElement.disableProperty().bind(dim.lessThan(16).not())

        buttonAddElement.setOnMouseClicked { addElement() }
        buttonRemoveElement.setOnMouseClicked { removeElement() }

        grid.hgap = 16.0
        grid.vgap = 16.0

        vbox.spacing = 8.0
        vbox.prefWidth = 200.0

        val toggleGroupHbox = HBox(toggleButtonVisModeManual, toggleButtonVisModelAuto)
        toggleGroupHbox.visibleProperty().bind(buttonCheckbox.selectedProperty())
        visModelAutoPane.visibleProperty().bind(toggleButtonVisModelAuto.selectedProperty().and(buttonCheckbox.selectedProperty()))
        visModeManualPane.visibleProperty().bind(toggleButtonVisModeManual.selectedProperty().and(buttonCheckbox.selectedProperty()))
        vbox.children.addAll(
            buttonAddElement,
            buttonRemoveElement,
            buttonLoadGraph,
            buttonGenerateGraph,
            buttonCheckbox,
            toggleGroupHbox,
            visModelAutoPane,
            visModeManualPane,
            buttonStart,
            grid,
        )
        children.add(vbox)
        setLeftAnchor(vbox, 0.0)
        setRightAnchor(vbox, 0.0)
        setTopAnchor(vbox, 0.0)
        setBottomAnchor(vbox, 0.0)

        for (i in 0 until 2) {
            addElement()
        }
    }
}
