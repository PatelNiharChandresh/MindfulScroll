package com.rudy.mindfulscroll.di

import com.rudy.mindfulscroll.data.repository.MonitoredAppRepositoryImpl
import com.rudy.mindfulscroll.data.repository.ThresholdConfigRepositoryImpl
import com.rudy.mindfulscroll.data.repository.UsageSessionRepositoryImpl
import com.rudy.mindfulscroll.data.repository.UserPreferencesRepositoryImpl
import com.rudy.mindfulscroll.domain.repository.MonitoredAppRepository
import com.rudy.mindfulscroll.domain.repository.ThresholdConfigRepository
import com.rudy.mindfulscroll.domain.repository.UsageSessionRepository
import com.rudy.mindfulscroll.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMonitoredAppRepository(
        impl: MonitoredAppRepositoryImpl
    ): MonitoredAppRepository

    @Binds
    abstract fun bindThresholdConfigRepository(
        impl: ThresholdConfigRepositoryImpl
    ): ThresholdConfigRepository

    @Binds
    abstract fun bindUsageSessionRepository(
        impl: UsageSessionRepositoryImpl
    ): UsageSessionRepository

    @Binds
    abstract fun bindUserPreferencesRepository(
        impl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}
