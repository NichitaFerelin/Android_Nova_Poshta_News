package com.ferelin.novaposhtanews.utils

fun <T> List<T>.takeIfOrEmpty(predicate: Boolean): List<T> {
    return if (predicate) this else emptyList()
}
