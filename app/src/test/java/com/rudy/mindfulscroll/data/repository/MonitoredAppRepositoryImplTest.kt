package com.rudy.mindfulscroll.data.repository

import com.rudy.mindfulscroll.data.local.dao.MonitoredAppDao
import com.rudy.mindfulscroll.data.local.entity.MonitoredAppEntity
import com.rudy.mindfulscroll.domain.model.MonitoredApp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MonitoredAppRepositoryImplTest {

    private lateinit var dao: MonitoredAppDao
    private lateinit var repository: MonitoredAppRepositoryImpl

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = MonitoredAppRepositoryImpl(dao)
    }

    @Test
    fun `getAll maps entities to domain models`() = runTest {
        val entities = listOf(
            MonitoredAppEntity("com.a", "App A", true, 100L),
            MonitoredAppEntity("com.b", "App B", false, 200L),
        )
        coEvery { dao.getAll() } returns flowOf(entities)

        val result = repository.getAll().first()

        assertEquals(2, result.size)
        assertEquals("com.a", result[0].packageName)
        assertEquals("App A", result[0].displayName)
        assertEquals(true, result[0].isActive)
        assertEquals("com.b", result[1].packageName)
        assertEquals(false, result[1].isActive)
    }

    @Test
    fun `getActive returns only active apps as domain models`() = runTest {
        val entities = listOf(
            MonitoredAppEntity("com.active", "Active", true, 100L),
        )
        coEvery { dao.getActive() } returns flowOf(entities)

        val result = repository.getActive().first()

        assertEquals(1, result.size)
        assertEquals("com.active", result[0].packageName)
    }

    @Test
    fun `getActivePackageNames returns set from dao`() = runTest {
        coEvery { dao.getActivePackageNames() } returns listOf("com.a", "com.b", "com.a")

        val result = repository.getActivePackageNames()

        assertEquals(setOf("com.a", "com.b"), result)
    }

    @Test
    fun `upsert converts domain to entity and delegates to dao`() = runTest {
        val app = MonitoredApp("com.test", "Test", true, 500L)

        repository.upsert(app)

        coVerify {
            dao.upsert(
                MonitoredAppEntity("com.test", "Test", true, 500L)
            )
        }
    }

    @Test
    fun `setActive delegates to dao`() = runTest {
        repository.setActive("com.test", false)

        coVerify { dao.setActive("com.test", false) }
    }

    @Test
    fun `removeUninstalled delegates to dao`() = runTest {
        val packages = listOf("com.a", "com.b")

        repository.removeUninstalled(packages)

        coVerify { dao.removeUninstalled(packages) }
    }
}
