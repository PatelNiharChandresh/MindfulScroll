package com.rudy.mindfulscroll.data.repository

import com.rudy.mindfulscroll.data.local.dao.UserPreferencesDao
import com.rudy.mindfulscroll.data.local.entity.UserPreferenceEntity
import com.rudy.mindfulscroll.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dao: UserPreferencesDao,
) : UserPreferencesRepository {

    override suspend fun getString(key: String, default: String): String =
        withContext(Dispatchers.IO) { dao.get(key) ?: default }

    override suspend fun getBoolean(key: String, default: Boolean): Boolean =
        withContext(Dispatchers.IO) { dao.get(key)?.toBooleanStrictOrNull() ?: default }

    override suspend fun getFloat(key: String, default: Float): Float =
        withContext(Dispatchers.IO) { dao.get(key)?.toFloatOrNull() ?: default }

    override suspend fun getInt(key: String, default: Int): Int =
        withContext(Dispatchers.IO) { dao.get(key)?.toIntOrNull() ?: default }

    override suspend fun setString(key: String, value: String) =
        withContext(Dispatchers.IO) { dao.set(UserPreferenceEntity(key, value)) }

    override suspend fun setBoolean(key: String, value: Boolean) =
        withContext(Dispatchers.IO) { dao.set(UserPreferenceEntity(key, value.toString())) }

    override suspend fun setFloat(key: String, value: Float) =
        withContext(Dispatchers.IO) { dao.set(UserPreferenceEntity(key, value.toString())) }

    override suspend fun setInt(key: String, value: Int) =
        withContext(Dispatchers.IO) { dao.set(UserPreferenceEntity(key, value.toString())) }

    override fun observeString(key: String): Flow<String?> = dao.observe(key)

    override fun observeBoolean(key: String): Flow<Boolean> =
        dao.observe(key).map { it?.toBooleanStrictOrNull() ?: false }
}
