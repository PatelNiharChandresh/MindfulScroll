package com.rudy.mindfulscroll.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    suspend fun getString(key: String, default: String = ""): String
    suspend fun getBoolean(key: String, default: Boolean = false): Boolean
    suspend fun getFloat(key: String, default: Float = 0f): Float
    suspend fun getInt(key: String, default: Int = 0): Int
    suspend fun setString(key: String, value: String)
    suspend fun setBoolean(key: String, value: Boolean)
    suspend fun setFloat(key: String, value: Float)
    suspend fun setInt(key: String, value: Int)
    fun observeString(key: String): Flow<String?>
    fun observeBoolean(key: String): Flow<Boolean>
}
