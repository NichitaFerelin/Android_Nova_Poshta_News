package com.ferelin.novaposhtanews.data.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.ferelin.novaposhtanews.UserSortPreferences
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

class UserSortPreferencesSerializer : Serializer<UserSortPreferences> {

    override val defaultValue: UserSortPreferences = UserSortPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSortPreferences {
        try {
            return UserSortPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read user sort preferences proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserSortPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}
