package com.rudy.mindfulscroll.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rudy.mindfulscroll.data.local.entity.UserPreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {

    @Query("SELECT value FROM user_preferences WHERE `key` = :key")
    suspend fun get(key: String): String?

    @Query("SELECT value FROM user_preferences WHERE `key` = :key")
    fun observe(key: String): Flow<String?>

    @Upsert
    suspend fun set(preference: UserPreferenceEntity)
}
