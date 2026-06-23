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

package com.altayyar.app.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.TypeConverters
import com.altayyar.app.data.local.Converters
import com.altayyar.app.entity.AccountWarning
import com.altayyar.app.entity.Notification
import com.altayyar.app.entity.RelationshipSeveranceEvent
import java.util.Date

@TypeConverters(Converters::class)
data class NotificationDataEntity(
    // id of the account logged into Tayyar this notifications belongs to
    val tayyarAccountId: Long,
    // null when placeholder
    val type: Notification.Type?,
    val id: String,
    @Embedded(prefix = "a_") val account: TimelineAccountEntity?,
    @Embedded(prefix = "s_") val status: TimelineStatusEntity?,
    @Embedded(prefix = "sa_") val statusAccount: TimelineAccountEntity?,
    @Embedded(prefix = "r_") val report: NotificationReportEntity?,
    @Embedded(prefix = "ra_") val reportTargetAccount: TimelineAccountEntity?,
    val event: RelationshipSeveranceEvent?,
    val moderationWarning: AccountWarning?,
    // relevant when it is a placeholder
    val loading: Boolean = false
)

@Entity(
    primaryKeys = ["id", "tayyarAccountId"],
    foreignKeys = (
        [
            ForeignKey(
                entity = TimelineAccountEntity::class,
                parentColumns = ["serverId", "tayyarAccountId"],
                childColumns = ["accountId", "tayyarAccountId"]
            ),
            ForeignKey(
                entity = TimelineStatusEntity::class,
                parentColumns = ["serverId", "tayyarAccountId"],
                childColumns = ["statusId", "tayyarAccountId"]
            ),
            ForeignKey(
                entity = NotificationReportEntity::class,
                parentColumns = ["serverId", "tayyarAccountId"],
                childColumns = ["reportId", "tayyarAccountId"]
            )
        ]
        ),
    indices = [
        Index("accountId", "tayyarAccountId"),
        Index("statusId", "tayyarAccountId"),
        Index("reportId", "tayyarAccountId"),
    ]
)
@TypeConverters(Converters::class)
data class NotificationEntity(
    // id of the account logged into Tayyar this notifications belongs to
    val tayyarAccountId: Long,
    // null when placeholder
    val type: Notification.Type?,
    val id: String,
    val accountId: String?,
    val statusId: String?,
    val reportId: String?,
    val event: RelationshipSeveranceEvent?,
    val moderationWarning: AccountWarning?,
    // relevant when it is a placeholder
    val loading: Boolean = false
)

@Entity(
    primaryKeys = ["serverId", "tayyarAccountId"],
    foreignKeys = (
        [
            ForeignKey(
                entity = TimelineAccountEntity::class,
                parentColumns = ["serverId", "tayyarAccountId"],
                childColumns = ["targetAccountId", "tayyarAccountId"]
            )
        ]
        ),
    indices = [
        Index("targetAccountId", "tayyarAccountId"),
    ]
)
@TypeConverters(Converters::class)
data class NotificationReportEntity(
    // id of the account logged into Tayyar this report belongs to
    val tayyarAccountId: Long,
    val serverId: String,
    val category: String,
    val statusIds: List<String>?,
    val createdAt: Date,
    val targetAccountId: String?
)
