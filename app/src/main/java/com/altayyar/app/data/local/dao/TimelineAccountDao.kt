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

package com.altayyar.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.altayyar.app.data.local.entity.TimelineAccountEntity

@Dao
abstract class TimelineAccountDao {

    @Insert(onConflict = REPLACE)
    abstract suspend fun insert(timelineAccountEntity: TimelineAccountEntity): Long

    @Query(
        """SELECT * FROM TimelineAccountEntity a
           WHERE a.serverId = :accountId
           AND a.tayyarAccountId = :tayyarAccountId"""
    )
    internal abstract suspend fun getAccount(tayyarAccountId: Long, accountId: String): TimelineAccountEntity?

    @Query("DELETE FROM TimelineAccountEntity WHERE tayyarAccountId = :tayyarAccountId")
    abstract suspend fun removeAllAccounts(tayyarAccountId: Long)

    /**
     * Cleans the TimelineAccountEntity table from accounts that are no longer referenced by either TimelineStatusEntity, HomeTimelineEntity or NotificationEntity
     * @param tayyarAccountId id of the user account for which to clean timeline accounts
     */
    @Query(
        """DELETE FROM TimelineAccountEntity WHERE tayyarAccountId = :tayyarAccountId
        AND serverId NOT IN
        (SELECT authorServerId FROM TimelineStatusEntity WHERE tayyarAccountId = :tayyarAccountId)
        AND serverId NOT IN
        (SELECT reblogAccountId FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND reblogAccountId IS NOT NULL)
        AND serverId NOT IN
        (SELECT accountId FROM NotificationEntity WHERE tayyarAccountId = :tayyarAccountId AND accountId IS NOT NULL)
        AND serverId NOT IN
        (SELECT targetAccountId FROM NotificationReportEntity WHERE tayyarAccountId = :tayyarAccountId AND targetAccountId IS NOT NULL)"""
    )
    abstract suspend fun cleanupAccounts(tayyarAccountId: Long)
}
