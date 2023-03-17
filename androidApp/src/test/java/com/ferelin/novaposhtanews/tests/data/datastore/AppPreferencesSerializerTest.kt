package com.ferelin.novaposhtanews.tests.data.datastore

import androidx.datastore.core.CorruptionException
import com.ferelin.novaposhtanews.appPreferences
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AppPreferencesSerializerTest {

    private val appPreferencesSerializer = AppPreferencesSerializer()

    @Test
    fun `Serializer returns correct default value`() {
        assertTrue {
            appPreferences { /*default*/ } == appPreferencesSerializer.defaultValue
        }
    }

    @Test
    fun `Writing and reading app preferences outputs correct value`() = runTest {
        val expectedPrefs = appPreferences { isNewsAutoLoadEnabled = true }
        val outputStream = ByteArrayOutputStream()
        expectedPrefs.writeTo(outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val actualPrefs = appPreferencesSerializer.readFrom(inputStream)
        assertTrue { expectedPrefs == actualPrefs }
    }

    @Test(expected = CorruptionException::class)
    fun `Reading invalid app preferences throws corruption exception`() = runTest {
        appPreferencesSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
    }
}
