package board

class SquareBoardImpl(override val width: Int) : SquareBoard {
    private val cells : Array<Array<Cell>>

    init {
        var cellsInitializer = arrayOf<Array<Cell>>()

        for (row in getRowsRange()) {
            var rowArray = arrayOf<Cell>()

            for (column in getColumnsRange()) {
                rowArray += Cell(row, column)
            }

            cellsInitializer += rowArray
        }

        cells = cellsInitializer
    }

    private fun inWidth(number: Int): Boolean {
        return number in getRowsRange()
    }

    private fun getRowsRange() = 1..width
    private fun getColumnsRange() = 1..width

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if ( !inWidth(i) )
            return null

        if ( !inWidth(j) )
            return null

        return cells[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell {
        require( inWidth(i) ) { "i should be in range 1..$width" }
        require( inWidth(j)) { "j should be in range 1..$width" }

        return cells[i - 1][j - 1]
    }

    override fun getAllCells(): Collection<Cell> {
        val allCells = mutableListOf<Cell>()

        for (i in getRowsRange()) {
            for (j in getColumnsRange()) {
                allCells += getCell(i, j)
            }
        }

        return allCells
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val cells = mutableListOf<Cell>()

        for (j in jRange) {
            val cell = getCellOrNull(i, j)
            if (cell != null) {
                cells += cell
            }
        }

        return cells
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val cells = mutableListOf<Cell>()

        for (i in iRange) {
            val cell = getCellOrNull(i, j)
            if (cell != null) {
                cells += cell
            }
        }

        return cells
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            Direction.UP -> getCellOrNull(i - 1, j)
            Direction.DOWN -> getCellOrNull(i + 1, j)
            Direction.LEFT -> getCellOrNull(i , j - 1)
            Direction.RIGHT -> getCellOrNull(i, j + 1)
            else -> throw IllegalArgumentException("Unknown direction: $direction.")
        }
    }
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

class GameBoardImpl<T>(
    override val width: Int,
    private val squareBoard: SquareBoard
) : GameBoard<T>, SquareBoard by squareBoard {
    private val map = mutableMapOf<Cell, T?>()

    override fun get(cell: Cell): T? {
        val cellFromBoard = squareBoard.getCell(cell.i, cell.j) // ensure the indices

        return map[cellFromBoard]
    }

    override fun set(cell: Cell, value: T?) {
        val cellFromBoard = squareBoard.getCell(cell.i, cell.j) // ensure the indices

        map[cellFromBoard] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return getAllCells().filter { cell -> predicate(map[cell]) }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        val filterValues: Map<Cell, T?> = map.filterValues(predicate)

        return if (filterValues.any()) filterValues.toList()[0].first else null
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return squareBoard
            .getAllCells()
            .any {
                cell -> predicate( map.get(cell) )
            }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return squareBoard
            .getAllCells()
            .all {
                cell -> predicate( map.get(cell) )
            }
    }
}

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width, SquareBoardImpl(width))