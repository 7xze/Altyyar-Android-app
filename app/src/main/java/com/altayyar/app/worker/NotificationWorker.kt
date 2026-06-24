/*
 * Copyright 2023 Tayyar Contributors
 *
 * This file is a part of Tayyar.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tayyar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tayyar; if not,
 * see <http://www.gnu.org/licenses>.
 */

package com.altayyar.app.worker

import android.app.Notification
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.altayyar.app.R
import com.altayyar.app.presentation.ui.feature.systemnotifications.NotificationFetcher
import com.altayyar.app.presentation.ui.feature.systemnotifications.NotificationService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val notificationsFetcher: NotificationFetcher,
    notificationService: NotificationService,
) : CoroutineWorker(appContext, params) {
    val notification: Notification = notificationService.createWorkerNotification(
        R.string.notification_notification_worker
    )

    override suspend fun doWork(): Result {
        val accountId = inputData.getLong(KEY_ACCOUNT_ID, 0).takeIf { it != 0L }
        notificationsFetcher.fetchAndShow(accountId)
        return Result.success()
    }

    override suspend fun getForegroundInfo() = ForegroundInfo(
        NotificationService.NOTIFICATION_ID_FETCH_NOTIFICATION,
        notification
    )

    companion object {
        const val KEY_ACCOUNT_ID = "accountId"
    }
}
