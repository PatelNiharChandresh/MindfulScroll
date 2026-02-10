package com.rudy.mindfulscroll.data.repository

import com.rudy.mindfulscroll.data.local.dao.UserPreferencesDao
import com.rudy.mindfulscroll.data.local.entity.UserPreferenceEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserPreferencesRepositoryImplTest {

    private lateinit var dao: UserPreferencesDao
    private lateinit var repository: UserPreferencesRepositoryImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = UserPreferencesRepositoryImpl(dao)
    }

    // --- getString ---

    @Test
    fun `getString returns stored value`() = runTest {
        coEvery { dao.get("key") } returns "value"

        val result = repository.getString("key", "default")

        assertEquals("value", result)
    }

    @Test
    fun `getString returns default when key not found`() = runTest {
        coEvery { dao.get("missing") } returns null

        val result = repository.getString("missing", "fallback")

        assertEquals("fallback", result)
    }

    // --- getBoolean ---

    @Test
    fun `getBoolean returns true when stored as true`() = runTest {
        coEvery { dao.get("flag") } returns "true"

        val result = repository.getBoolean("flag", false)

        assertEquals(true, result)
    }

    @Test
    fun `getBoolean returns false when stored as false`() = runTest {
        coEvery { dao.get("flag") } returns "false"

        val result = repository.getBoolean("flag", true)

        assertEquals(false, result)
    }

    @Test
    fun `getBoolean returns default when value is not parseable`() = runTest {
        coEvery { dao.get("flag") } returns "notboolean"

        val result = repository.getBoolean("flag", true)

        assertEquals(true, result)
    }

    @Test
    fun `getBoolean returns default when key not found`() = runTest {
        coEvery { dao.get("missing") } returns null

        val result = repository.getBoolean("missing", false)

        assertEquals(false, result)
    }

    // --- getFloat ---

    @Test
    fun `getFloat returns stored float value`() = runTest {
        coEvery { dao.get("pos") } returns "0.75"

        val result = repository.getFloat("pos", 0.5f)

        assertEquals(0.75f, result)
    }

    @Test
    fun `getFloat returns default when value is not parseable`() = runTest {
        coEvery { dao.get("pos") } returns "abc"

        val result = repository.getFloat("pos", 0.5f)

        assertEquals(0.5f, result)
    }

    @Test
    fun `getFloat returns default when key not found`() = runTest {
        coEvery { dao.get("missing") } returns null

        val result = repository.getFloat("missing", 1.0f)

        assertEquals(1.0f, result)
    }

    // --- getInt ---

    @Test
    fun `getInt returns stored int value`() = runTest {
        coEvery { dao.get("count") } returns "45"

        val result = repository.getInt("count", 0)

        assertEquals(45, result)
    }

    @Test
    fun `getInt returns default when value is not parseable`() = runTest {
        coEvery { dao.get("count") } returns "xyz"

        val result = repository.getInt("count", 10)

        assertEquals(10, result)
    }

    @Test
    fun `getInt returns default when key not found`() = runTest {
        coEvery { dao.get("missing") } returns null

        val result = repository.getInt("missing", 99)

        assertEquals(99, result)
    }

    // --- set methods ---

    @Test
    fun `setString stores value via dao`() = runTest {
        repository.setString("key", "value")

        coVerify { dao.set(UserPreferenceEntity("key", "value")) }
    }

    @Test
    fun `setBoolean stores true as string`() = runTest {
        repository.setBoolean("flag", true)

        coVerify { dao.set(UserPreferenceEntity("flag", "true")) }
    }

    @Test
    fun `setBoolean stores false as string`() = runTest {
        repository.setBoolean("flag", false)

        coVerify { dao.set(UserPreferenceEntity("flag", "false")) }
    }

    @Test
    fun `setFloat stores float as string`() = runTest {
        repository.setFloat("pos", 0.75f)

        coVerify { dao.set(UserPreferenceEntity("pos", "0.75")) }
    }

    @Test
    fun `setInt stores int as string`() = runTest {
        repository.setInt("count", 42)

        coVerify { dao.set(UserPreferenceEntity("count", "42")) }
    }

    // --- observe methods ---

    @Test
    fun `observeString emits stored value`() = runTest {
        coEvery { dao.observe("key") } returns flowOf("hello")

        val result = repository.observeString("key").first()

        assertEquals("hello", result)
    }

    @Test
    fun `observeString emits null when key not found`() = runTest {
        coEvery { dao.observe("missing") } returns flowOf(null)

        val result = repository.observeString("missing").first()

        assertEquals(null, result)
    }

    @Test
    fun `observeBoolean emits true when stored`() = runTest {
        coEvery { dao.observe("flag") } returns flowOf("true")

        val result = repository.observeBoolean("flag").first()

        assertEquals(true, result)
    }

    @Test
    fun `observeBoolean emits false when null`() = runTest {
        coEvery { dao.observe("flag") } returns flowOf(null)

        val result = repository.observeBoolean("flag").first()

        assertEquals(false, result)
    }

    @Test
    fun `observeBoolean emits false when not parseable`() = runTest {
        coEvery { dao.observe("flag") } returns flowOf("garbage")

        val result = repository.observeBoolean("flag").first()

        assertEquals(false, result)
    }
}
