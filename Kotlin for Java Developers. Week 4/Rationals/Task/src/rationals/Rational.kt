package rationals

import java.math.BigInteger

class Rational(numerator: BigInteger, denominator: BigInteger) : Comparable<Rational> {

    val numerator: BigInteger
    val denominator: BigInteger

    init {
        require(denominator != BigInteger.ZERO) { // throws IAE with lazy-evaluated message
            "denominator cannot be 0"
        }

        val gcd = numerator.abs().gcd(denominator.abs())

        val sign = numerator.signum() * denominator.signum()

        this.numerator = (numerator.abs() / gcd) * sign.toBigInteger()
        this.denominator = denominator.abs() / gcd
    }

    operator fun plus(other: Rational) : Rational {
        val newNumerator =
              (this.numerator * other.denominator) +
              (other.numerator * this.denominator)

        val commonDenominator = this.denominator * other.denominator

        return Rational(newNumerator, commonDenominator)
    }

    operator fun minus(other: Rational) : Rational {
        return this.plus(-other)
    }

    operator fun times(other: Rational) : Rational {
        return Rational(
            this.numerator * other.numerator
            , this.denominator * other.denominator
        )
    }

    operator fun div(other: Rational) : Rational {
        return Rational(
            this.numerator * other.denominator
            , this.denominator * other.numerator
        )
    }

    operator fun unaryMinus() : Rational {
        return Rational(-numerator, denominator)
    }


    override fun compareTo(other: Rational): Int {
        val difference = this.minus(other)

        return difference.numerator.signum()
    }

    override fun toString(): String {
        if (denominator == 1.toBigInteger())
            return numerator.toString()

        return "$numerator/$denominator"
    }

    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false

        if (!(other is Rational))
            return false

        return (this.numerator == other.numerator) &&
            (this.denominator == other.denominator)
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }
}


infix fun Int.divBy(divisor: Int) : Rational {
    return Rational(this.toBigInteger(), divisor.toBigInteger())
}

infix fun Long.divBy(divisor: Long) : Rational {
    return Rational(this.toBigInteger(), divisor.toBigInteger())
}

infix fun BigInteger.divBy(divisor: BigInteger) : Rational {
    return Rational(this, divisor)
}

fun String?.toRational() : Rational {
    if (this.isNullOrBlank()) {
        throw IllegalArgumentException("Cannot convert from null or blank String.")
    }

    val splitNumbers = this.split("/")

    if (splitNumbers.size == 1) {
        val numerator = splitNumbers[0].toBigInteger()
        return Rational(numerator, 1.toBigInteger())
    }

    if (splitNumbers.size == 2) {
        val numerator = splitNumbers[0].toBigInteger()
        val denominator = splitNumbers[1].toBigInteger()
        return Rational(numerator, denominator)
    }

    throw NumberFormatException("Zero or more than two / parts.")
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}