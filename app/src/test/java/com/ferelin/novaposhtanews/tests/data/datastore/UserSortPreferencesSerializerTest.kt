package com.ferelin.novaposhtanews.tests.data.datastore

import androidx.datastore.core.CorruptionException
import com.ferelin.novaposhtanews.data.datastore.serializer.UserSortPreferencesSerializer
import com.ferelin.novaposhtanews.userSortPreferences
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class UserSortPreferencesSerializerTest {

    private val userPreferencesSerializer = UserSortPreferencesSerializer()

    @Test
    fun `Serializer returns correct default value`() {
        assertTrue {
            userSortPreferences { /*default*/ } == userPreferencesSerializer.defaultValue
        }
    }

    @Test
    fun `Writing and reading user sort preferences outputs correct value`() = runTest {
        val expectedPrefs = userSortPreferences { isMdNewsEnabled = true }
        val outputStream = ByteArrayOutputStream()
        expectedPrefs.writeTo(outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val actualPrefs = userPreferencesSerializer.readFrom(inputStream)
        assertTrue { expectedPrefs == actualPrefs }
    }

    @Test(expected = CorruptionException::class)
    fun `Reading invalid user sort preferences throws corruption exception`() = runTest {
        userPreferencesSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
    }
}
