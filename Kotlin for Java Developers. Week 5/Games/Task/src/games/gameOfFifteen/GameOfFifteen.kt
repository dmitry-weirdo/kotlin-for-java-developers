package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game
import java.lang.IllegalArgumentException


class GameImpl(val initializer: GameOfFifteenInitializer, val board: GameBoard<Int?>) : Game {

    override fun initialize() {
        val permutation = initializer.initialPermutation

        board.getAllCells().forEachIndexed { index, cell ->
            board.set(cell, permutation.getOrNull(index))
        }
    }

    private fun getEmptyCell() : Cell {
        return board.find { value -> value == null }!!
    }

    override fun canMove(): Boolean {

        return true // todo: implement method
    }

    override fun hasWon(): Boolean {
        val lastIndex = board.width * board.width - 1;

        board.getAllCells().forEachIndexed { index, cell ->
            val cellValue = board.get(cell)

            if (index == lastIndex) {
                if (cellValue != null) {
                    return false
                }
            }
            else {
                if (cellValue != index + 1) {
                    return false
                }
            }
        }

        return true
    }

    override fun processMove(direction: Direction) {
        val emptyCell = getEmptyCell()

        when (direction) {
            Direction.UP -> {
                with (board) {
                    val neighbourDown = emptyCell.getNeighbour(Direction.DOWN) // visible only in board

                    if (neighbourDown != null) {
                        // from the empty cell to the top
                        val columnToMove = board.getColumn(emptyCell.i..neighbourDown.i, emptyCell.j)
                        move(columnToMove)
                    }
                }
            }

            Direction.DOWN -> {
                with (board) {
                    val neighbourUp = emptyCell.getNeighbour(Direction.UP) // visible only in board

                    if (neighbourUp != null) {
                        // from the empty cell to the bottom
                        val columnToMove = board.getColumn(emptyCell.i downTo neighbourUp.i, emptyCell.j)
                        move(columnToMove)
                    }
                }
            }

            Direction.LEFT -> {
                with (board) {
                    val neighbourRight = emptyCell.getNeighbour(Direction.RIGHT) // visible only in board

                    if (neighbourRight != null) {
                        // from the empty cell to the right
                        val rowToMove = board.getRow(emptyCell.i, emptyCell.j..neighbourRight.j)
                        move(rowToMove)
                    }
                }
            }

            Direction.RIGHT -> {
                with (board) {
                    val neighbourLeft = emptyCell.getNeighbour(Direction.LEFT) // visible only in board

                    if (neighbourLeft != null) {
                        // from the empty cell to the right
                        val rowToMove = board.getRow(emptyCell.i, emptyCell.j downTo neighbourLeft.j)
                        move(rowToMove)
                    }

                }
            }
        }
    }

    private fun move(cellsToMove : List<Cell>) {
        // assure first cell is an empty cell
        val firstCell = cellsToMove[0]

        val emptyCell = getEmptyCell()

        if (firstCell != emptyCell)
            throw IllegalArgumentException("Cell $firstCell is not an emptyCell, which is $emptyCell")


        for (index in 0 until cellsToMove.size - 1) {
            // assign current to next in the list

            val newValue = board.get(cellsToMove[index + 1])

            board.set(cellsToMove[index], newValue)
        }

        // assign last cell to empty
        board.set(cellsToMove.last(), null)
    }

    override fun get(i: Int, j: Int): Int? {
        val cell = board.getCell(i, j)
        return board.get(cell)
    }
}

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
   GameImpl(initializer, createGameBoard(4))


