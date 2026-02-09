package com.rudy.mindfulscroll.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Binds repository interfaces to their implementations.
 * Full implementation in Phase 2 (WBS 2.6 - 2.9).
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule
