package com.rudy.mindfulscroll.domain.repository

import com.rudy.mindfulscroll.domain.model.InstalledApp

interface InstalledAppRepository {
    suspend fun getInstalledApps(): List<InstalledApp>
}
