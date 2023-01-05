package com.ferelin.novaposhtanews.utils

import java.nio.charset.StandardCharsets
import okio.buffer
import okio.source

internal fun ClassLoader.readFileContent(file: String): String {
    val inputStream = getResourceAsStream(file)
    val source = inputStream!!.source().buffer()
    return source.readString(StandardCharsets.UTF_8)
}
