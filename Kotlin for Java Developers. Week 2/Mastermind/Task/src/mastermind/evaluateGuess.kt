package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var rightPosition = 0
    var wrongPosition = 0

    val usedIndices = HashSet<Int>()

    for ((indexGuess, valueGuess) in guess.withIndex()) {
        if (secret[indexGuess] == valueGuess) {
            rightPosition++
            usedIndices.add(indexGuess)
            continue
        }
    }

    for ((indexGuess, valueGuess) in guess.withIndex()) {
        if (secret[indexGuess] == valueGuess) { // already counted in rightPosition
            continue
        }

        for ((indexSecret, valueSecret) in secret.withIndex()) {
            if (indexSecret in usedIndices) {
                continue
            }

            if (valueSecret == valueGuess) {
                wrongPosition++
                usedIndices.add(indexSecret)
                break
            }
        }
    }

    return Evaluation(rightPosition, wrongPosition)
}