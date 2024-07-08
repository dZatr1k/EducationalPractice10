package ru.squad10

import javafx.application.Platform
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.stage.FileChooser
import ru.squad10.algorithm.LaunchType
import ru.squad10.dto.Edge
import ru.squad10.dto.Graph
import ru.squad10.dto.Vertex
import ru.squad10.log.Logger
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.min

class MatrixIOPane(private val representation: GraphRepresentation, private val graphProperty: ReadOnlyObjectWrapper<Graph>) : AnchorPane() {
    private var dim = SimpleIntegerProperty(0)
    private val vbox = VBox()
    private val grid = GridPane()
    private val checkboxes = mutableMapOf<Pair<Int, Int>, CheckBox>()
    private val labels = mutableMapOf<Pair<Int, Int>, Label>()
    private val vertexCache = mutableMapOf<Int, Vertex>()
    private var visualizationState: LaunchType = LaunchType.DEFAULT
    private var blockableUI: MutableSet<Node> = mutableSetOf()

    val smallStepButton: Button = Button("Малый шаг")

    private fun addCheckbox(cb: CheckBox, i: Int, j: Int) {
        grid.add(cb, j + 1, i + 1)
        checkboxes[(i to j)] = cb

        val edge = Edge(vertexCache[i]!!, vertexCache[j]!!)

        cb.selectedProperty().addListener { _, _, cur ->
            if (cur) {
                Platform.runLater{
                    AlgoApp.logger.log(Logger.Level.INFO, "Добавление дуги: $edge")
                }
                graphProperty.set(
                    Graph(
                        graphProperty.get().vertices,
                        graphProperty.get().edges + edge
                    )
                )
            } else {
                resetCheckboxesStyle()
                AlgoApp.logger.log(Logger.Level.INFO, "Удаление дуги: $edge")
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
        AlgoApp.logger.log(Logger.Level.INFO, "Добавление элемента: $name")
        vertexCache[dim.get()] = Vertex(name)

        dim.set(dim.get() + 1)

        var lb = Label(name)
        labels[0 to dim.get()] = lb
        grid.add(lb, 0, dim.get())
        lb = Label(name)
        labels[dim.get() to 0] = lb
        grid.add(lb, dim.get(), 0)

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
        resetCheckboxesStyle()
    }

    private fun removeElement() {
        val name = ('A' + dim.get() - 1).toString()
        AlgoApp.logger.log(Logger.Level.INFO, "Удаление элемента: $name")
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
        resetCheckboxesStyle()
    }

    private fun clearGraph() {
        AlgoApp.logger.log(Logger.Level.INFO, "Очистка графа")
        while (dim.get() != 0) {
            removeElement()
        }
    }

    private fun deleteLoop(){
        for (i in vertexCache.values) {
            for (j in vertexCache.values) {
                if (i == j){
                    val fromIndex = i.name.first() - 'A'
                    val toIndex = j.name.first() - 'A'
                    checkboxes[fromIndex to toIndex]!!.isSelected = false
                }
            }
        }

        resetCheckboxesStyle()
    }

    private fun makeRandomGraph(size: Int, edgeNumber: Int = -1) {
        when{
            edgeNumber == -1 -> AlgoApp.logger.log(Logger.Level.INFO, "Создание рандомного графа. Размер: $size, Количество дуг: неограничено")
            else -> AlgoApp.logger.log(Logger.Level.INFO, "Создание рандомного графа. Размер: $size, Количество дуг: $edgeNumber")
        }
        val rand = ThreadLocalRandom.current()
        clearGraph()

        for (i in 0 until size) {
            addElement()
        }

        if (edgeNumber == -1) {
            for (i in vertexCache.values) {
                for (j in vertexCache.values) {
                    if (i == j) continue
                    if (rand.nextBoolean()) {
                        val fromIndex = i.name.first() - 'A'
                        val toIndex = j.name.first() - 'A'
                        checkboxes[fromIndex to toIndex]!!.isSelected = true
                    }
                }
            }
        } else {
            val maxNumberOfEdges = size * size - size
            val edgeArray = ArrayList<Pair<Int, Int>>()
            for (i in 0 until size) {
                for (j in 0 until size) {
                    if (i != j) edgeArray.add(Pair(i, j))
                }
            }
            edgeArray.shuffle()
            for (i in 0 until min(edgeNumber, maxNumberOfEdges)) {
                val fromIndex = edgeArray[i].first
                val toIndex = edgeArray[i].second
                checkboxes[fromIndex to toIndex]!!.isSelected = true
            }
        }
    }


    private fun makeGraphFromFile(path: Path) {
        AlgoApp.logger.log(Logger.Level.INFO, "Создание графа по файлу")
        clearGraph()

        val fileStrings =
            Files.readAllLines(path)
                .filterNot { it.isBlank() }
                .map { it.trim() }
                .map { it.split(whiteSpaceRegex) }
                .map { it.map { it.toInt() } }

        for (i in 0 until fileStrings.size) {
            addElement()
        }
        for (i in 0 until fileStrings.size) {
            for (j in 0 until fileStrings[i].size) {
                if (i == j) continue
                if (fileStrings[i][j] == 1) {
                    checkboxes[i to j]!!.isSelected = true
                }
            }
        }
    }

    fun resetCheckboxesStyle(){
        for(checkbox in checkboxes){
            checkbox.value?.lookup(".box")?.style = null
        }
    }

    fun getCheckboxes(): MutableMap<Pair<Int, Int>, CheckBox>{
        return checkboxes
    }

    fun setCheckboxColor(fromIndex: Int, toIndex: Int, color: String){
        checkboxes[fromIndex to toIndex]!!.lookup(".box").style = "-fx-background-color: $color;"
    }

    fun resetCheckboxStyle(fromIndex: Int, toIndex: Int){
        checkboxes[fromIndex to toIndex]!!.lookup(".box").style = null
    }

    fun setGridLabelColor(fromIndex: Int, toIndex: Int, color: String){
        labels[fromIndex to toIndex]!!.style = "-fx-text-fill: $color;"
    }

    fun resetGridLabelStyle(fromIndex: Int, toIndex: Int){
        labels[fromIndex to toIndex]!!.style = null
    }

    fun getAutomaticStepDelayInMills(): Long{
        return 250
    }

    fun lockUI(){
        for(node in blockableUI){
            node.isDisable = true
        }
    }

    fun unlockUI(){
        for(node in blockableUI){
            node.isDisable = false
        }
    }

    private val fileLineRegex = "^(?:[01]\\s+)*[01]\$".toRegex()
    private val whiteSpaceRegex = "\\s+".toRegex()

    init {
        val buttonAddElement = Button("Добавить")
        val buttonRemoveElement = Button("Удалить")
        val buttonLoadGraph = Button("Загрузка")
        val buttonGenerateGraph = Button("Рандом")
        val buttonCheckbox = CheckBox("Показ работы")
        val buttonStart = Button("Запуск")
        val buttonClear = Button("Очистить")
        val buttonDeleteLoop = Button("Удалить петли")

        val paramVertexForRandomMatrix = TextField().apply {
            promptText = "Кол-во вершин"
        }
        val paramEdgeForRandomMatrix = TextField().apply {
            promptText = "Кол-во ребер"
        }

        blockableUI.add(buttonStart)

        val toggleGroupVisMode = ToggleGroup()
        val toggleButtonVisModeManual = ToggleButton("Ручной")
        val toggleButtonVisModelAuto = ToggleButton("Автоматический")

        toggleButtonVisModelAuto.selectedProperty().addListener{ _, _, cur ->
            if(cur && buttonCheckbox.isSelected)
                visualizationState = LaunchType.AUTO
        }

        toggleButtonVisModeManual.selectedProperty().addListener{ _, _, cur ->
            if(cur && buttonCheckbox.isSelected)
                visualizationState = LaunchType.MANUAL
        }

        toggleButtonVisModeManual.isSelected = true

        toggleGroupVisMode.toggles.addAll(toggleButtonVisModeManual, toggleButtonVisModelAuto)

        buttonCheckbox.selectedProperty().addListener {_, _, cur ->
            visualizationState = if(!cur){
                LaunchType.DEFAULT
            } else {
                if(toggleButtonVisModeManual.isSelected){
                    LaunchType.MANUAL
                } else if(toggleButtonVisModelAuto.isSelected){
                    LaunchType.AUTO
                } else{
                    LaunchType.DEFAULT
                }
            }
        }

        val visModelAutoTimeSelector = Slider(0.25, 10.0, 1.0)

        val visModelAutoPane = VBox(
            Label("Время шага"),
            visModelAutoTimeSelector
        )

        val visModeManualPane = HBox(
            smallStepButton,
            Button("Средний шаг"),
            Button("Большой шаг"),
        )

        buttonRemoveElement.disableProperty().bind(dim.greaterThan(2).not())
        buttonAddElement.disableProperty().bind(dim.lessThan(16).not())

        buttonAddElement.setOnMouseClicked { addElement() }
        buttonRemoveElement.setOnMouseClicked { removeElement() }
        buttonStart.setOnMouseClicked {
            AlgoApp.logger.log(Logger.Level.INFO, "Запуск алгоритма")
            representation.applyAlgorithm(visualizationState)
        }


        grid.hgap = 16.0
        grid.vgap = 16.0

        vbox.spacing = 8.0
        vbox.prefWidth = 200.0

        val toggleGroupHbox = HBox(toggleButtonVisModeManual, toggleButtonVisModelAuto)
        toggleGroupHbox.visibleProperty().bind(buttonCheckbox.selectedProperty())
        visModelAutoPane.visibleProperty()
            .bind(toggleButtonVisModelAuto.selectedProperty().and(buttonCheckbox.selectedProperty()))
        visModeManualPane.visibleProperty()
            .bind(toggleButtonVisModeManual.selectedProperty().and(buttonCheckbox.selectedProperty()))
        val visModesStackPane = StackPane(visModelAutoPane, visModeManualPane)

        buttonClear.setOnMouseClicked { clearGraph() }
        buttonDeleteLoop.setOnMouseClicked { deleteLoop() }

        val loadGraphFileChooser = FileChooser()
        loadGraphFileChooser.title = "Выберете файл"
        loadGraphFileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Text Files", "*.txt"))

        buttonLoadGraph.setOnMouseClicked {
            var file = loadGraphFileChooser.showOpenDialog(AlgoApp.stage)
            val fileStrings = Files.readAllLines(file.toPath()).filterNot { it.isBlank() }.map { it.trim() }
            for (string in fileStrings) {
                if (!fileLineRegex.matches(string)) {
                    file = null
                }
                if (string.split(whiteSpaceRegex).size != fileStrings.size) {
                    file = null
                }
            }
            if (file != null) {
                println("Выбран файл: $file")
                makeGraphFromFile(file.toPath())
            } else {
                println("Выбран некорректный файл")
            }

        }

        val deleteGraphHBox = HBox(10.0, buttonRemoveElement, buttonClear, buttonDeleteLoop)

        paramVertexForRandomMatrix.maxWidth = 100.0
        paramEdgeForRandomMatrix.maxWidth = 100.0
        val generateGraphHBox = HBox(10.0, buttonGenerateGraph, paramVertexForRandomMatrix, paramEdgeForRandomMatrix)
        buttonGenerateGraph.setOnMouseClicked {
            val vertexCount = paramVertexForRandomMatrix.text.toInt()
            val edgeCount = paramEdgeForRandomMatrix.text
            println(edgeCount)
            when(edgeCount){
                "" -> if (vertexCount in 0..16) makeRandomGraph(vertexCount)
                else -> if (vertexCount in 0..16 && edgeCount.toInt() >=0) makeRandomGraph(vertexCount, edgeCount.toInt())
            }
        }

        vbox.children.addAll(
            buttonAddElement,
            deleteGraphHBox,
            buttonLoadGraph,
            generateGraphHBox,
            buttonCheckbox,
            toggleGroupHbox,
            visModesStackPane,
            buttonStart,
            grid,
        )

        visModesStackPane.managedProperty().bind(buttonCheckbox.selectedProperty())

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
