package games.gameOfFifteen

interface GameOfFifteenInitializer {
    /*
     * Even permutation of numbers 1..15
     * used to initialized the first 15 cells on a board.
     * The last cell is empty.
     */
    val initialPermutation: List<Int>
}

class RandomGameInitializer : GameOfFifteenInitializer {
    /*
     * Generate a random permutation from 1 to 15.
     * `shuffled()` function might be helpful.
     * If the permutation is not even, make it even (for instance,
     * by swapping two numbers).
     */
    override val initialPermutation by lazy {
        val shuffled = (1..15).toList().shuffled().toMutableList()

        if ( !isEven(shuffled) ) {
            makeEven(shuffled)
        }

        shuffled
    }

    private fun makeEven(list : MutableList<Int>) {
        for (i in 1 until list.size) {
            for (j in (i + 1)..list.size) {
                if (list[i - 1] > list[j - 1]) {
                    val tmp = list[i - 1]
                    list[i - 1] = list[j - 1]
                    list[j - 1] = tmp

                    return
                }
            }
        }
    }
}

