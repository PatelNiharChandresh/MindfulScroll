package com.rudy.mindfulscroll.data.repository

import com.rudy.mindfulscroll.data.local.dao.UsageSessionDao
import com.rudy.mindfulscroll.data.local.entity.UsageSessionEntity
import com.rudy.mindfulscroll.domain.model.UsageSession
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UsageSessionRepositoryImplTest {

    private lateinit var dao: UsageSessionDao
    private lateinit var repository: UsageSessionRepositoryImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = UsageSessionRepositoryImpl(dao)
    }

    @Test
    fun `insert converts domain to entity and returns id`() = runTest {
        val session = UsageSession(
            appPackageName = "com.test",
            startTimestamp = 1000L,
            endTimestamp = 2000L,
            durationSeconds = 1000,
            maxThresholdReached = 3,
            throbActivated = true,
        )
        coEvery { dao.insert(any()) } returns 42L

        val result = repository.insert(session)

        assertEquals(42L, result)
        coVerify {
            dao.insert(
                UsageSessionEntity(
                    id = 0,
                    appPackageName = "com.test",
                    startTimestamp = 1000L,
                    endTimestamp = 2000L,
                    durationSeconds = 1000,
                    maxThresholdReached = 3,
                    throbActivated = true,
                )
            )
        }
    }

    @Test
    fun `getRecent maps entities to domain models`() = runTest {
        val entities = listOf(
            UsageSessionEntity(1, "com.a", 2000L, 3000L, 1000, 2, false),
            UsageSessionEntity(2, "com.b", 1000L, 2000L, 800, 1, false),
        )
        coEvery { dao.getRecent(10) } returns entities

        val result = repository.getRecent(10)

        assertEquals(2, result.size)
        assertEquals("com.a", result[0].appPackageName)
        assertEquals(1L, result[0].id)
        assertEquals("com.b", result[1].appPackageName)
        assertEquals(2L, result[1].id)
    }

    @Test
    fun `getRecent returns empty list when no sessions`() = runTest {
        coEvery { dao.getRecent(100) } returns emptyList()

        val result = repository.getRecent()

        assertEquals(emptyList<UsageSession>(), result)
    }

    @Test
    fun `deleteOlderThan delegates to dao`() = runTest {
        val cutoff = 1000000L

        repository.deleteOlderThan(cutoff)

        coVerify { dao.deleteOlderThan(cutoff) }
    }
}
