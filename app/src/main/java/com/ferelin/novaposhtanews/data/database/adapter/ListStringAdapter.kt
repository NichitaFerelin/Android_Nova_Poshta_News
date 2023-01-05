package com.ferelin.novaposhtanews.data.database.adapter

import com.squareup.sqldelight.ColumnAdapter

const val LIST_STRING_ADAPTER_SEPARATOR = "|"

class ListStringAdapter : ColumnAdapter<List<String>, String> {

    override fun decode(databaseValue: String): List<String> {
        return if (databaseValue.isEmpty()) {
            emptyList()
        } else {
            databaseValue.split(LIST_STRING_ADAPTER_SEPARATOR)
        }
    }

    override fun encode(value: List<String>): String {
        return value.joinToString(LIST_STRING_ADAPTER_SEPARATOR)
    }
}
