package com.ferelin.novaposhtanews.utils

private const val ROUND_TO_BIGGEST_TEN_FACTOR = 9
private const val BIGGEST_TEN = 10

object IntTransformUtils {

    fun roundToBiggestTen(number: Int): Int {
        return (number + ROUND_TO_BIGGEST_TEN_FACTOR) / BIGGEST_TEN * BIGGEST_TEN
    }
}
