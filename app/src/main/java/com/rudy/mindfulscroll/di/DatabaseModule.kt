package com.rudy.mindfulscroll.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Provides the Room database instance and all DAOs.
 * Full implementation in Phase 2 (WBS 2.5).
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule
