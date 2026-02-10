package com.rudy.mindfulscroll.di

import android.content.Context
import androidx.room.Room
import com.rudy.mindfulscroll.data.local.MindfulScrollDatabase
import com.rudy.mindfulscroll.data.local.dao.MonitoredAppDao
import com.rudy.mindfulscroll.data.local.dao.ThresholdConfigDao
import com.rudy.mindfulscroll.data.local.dao.UsageSessionDao
import com.rudy.mindfulscroll.data.local.dao.UserPreferencesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MindfulScrollDatabase =
        Room.databaseBuilder(
            context,
            MindfulScrollDatabase::class.java,
            "mindfulscroll.db"
        ).build()

    @Provides
    fun provideMonitoredAppDao(db: MindfulScrollDatabase): MonitoredAppDao =
        db.monitoredAppDao()

    @Provides
    fun provideThresholdConfigDao(db: MindfulScrollDatabase): ThresholdConfigDao =
        db.thresholdConfigDao()

    @Provides
    fun provideUsageSessionDao(db: MindfulScrollDatabase): UsageSessionDao =
        db.usageSessionDao()

    @Provides
    fun provideUserPreferencesDao(db: MindfulScrollDatabase): UserPreferencesDao =
        db.userPreferencesDao()
}
