/* Copyright 2024 Tayyar Contributors
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
 * see <http://www.gnu.org/licenses>. */

package com.altayyar.app.data.local

import androidx.room.withTransaction
import com.altayyar.app.data.local.entity.ConversationEntity
import com.altayyar.app.data.local.entity.HomeTimelineEntity
import com.altayyar.app.data.local.entity.NotificationEntity
import com.altayyar.app.data.local.entity.NotificationPolicyEntity
import com.altayyar.app.data.local.entity.NotificationReportEntity
import com.altayyar.app.data.local.entity.TimelineAccountEntity
import com.altayyar.app.data.local.entity.TimelineStatusEntity
import javax.inject.Inject

class DatabaseCleaner @Inject constructor(
    private val db: AppDatabase
) {
    /**
     * Cleans the [HomeTimelineEntity], [TimelineStatusEntity], [TimelineAccountEntity], [NotificationEntity] and [NotificationReportEntity] tables from old entries.
     * Should be regularly run to prevent the database from growing too big.
     * @param tayyarAccountId id of the account for which to clean tables
     * @param timelineLimit how many timeline items to keep
     * @param notificationLimit how many notifications to keep
     */
    suspend fun cleanupOldData(
        tayyarAccountId: Long,
        timelineLimit: Int,
        notificationLimit: Int
    ) {
        db.withTransaction {
            // the order here is important - foreign key constraints must not be violated
            db.notificationsDao().cleanupNotifications(tayyarAccountId, notificationLimit)
            db.notificationsDao().cleanupReports(tayyarAccountId)
            db.timelineDao().cleanupHomeTimeline(tayyarAccountId, timelineLimit)
            db.timelineStatusDao().cleanupStatuses(tayyarAccountId)
            db.timelineAccountDao().cleanupAccounts(tayyarAccountId)
        }
    }

    /**
     * Deletes everything from the [HomeTimelineEntity], [TimelineStatusEntity], [TimelineAccountEntity], [NotificationEntity],
     * [NotificationReportEntity],  [ConversationEntity] and [NotificationPolicyEntity] tables for one user.
     * Intended to be used when a user logs out.
     * @param tayyarAccountId id of the account for which to clean tables
     */
    suspend fun cleanupEverything(tayyarAccountId: Long) {
        db.withTransaction {
            // the order here is important - foreign key constraints must not be violated
            db.notificationsDao().removeAllNotifications(tayyarAccountId)
            db.notificationsDao().removeAllReports(tayyarAccountId)
            db.timelineDao().removeAllHomeTimelineItems(tayyarAccountId)
            db.timelineStatusDao().removeAllStatuses(tayyarAccountId)
            db.timelineAccountDao().removeAllAccounts(tayyarAccountId)
            db.conversationDao().deleteForAccount(tayyarAccountId)
            db.notificationPolicyDao().deleteForAccount(tayyarAccountId)
        }
    }
}
