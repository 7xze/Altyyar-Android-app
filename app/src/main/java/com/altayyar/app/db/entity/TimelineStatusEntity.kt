/* Copyright 2021 Tayyar Contributors
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

package com.altayyar.app.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.TypeConverters
import com.altayyar.app.db.Converters
import com.altayyar.app.entity.Attachment
import com.altayyar.app.entity.Emoji
import com.altayyar.app.entity.FilterResult
import com.altayyar.app.entity.HashTag
import com.altayyar.app.entity.Poll
import com.altayyar.app.entity.PreviewCard
import com.altayyar.app.entity.Status

/**
 * Entity for caching status data. Used within home timelines and notifications.
 * The information if a status is a reblog is not stored here but in [HomeTimelineEntity].
 */
@Entity(
    primaryKeys = ["serverId", "tayyarAccountId"],
    foreignKeys = (
        [
            ForeignKey(
                entity = TimelineAccountEntity::class,
                parentColumns = ["serverId", "tayyarAccountId"],
                childColumns = ["authorServerId", "tayyarAccountId"]
            )
        ]
        ),
    // Avoiding rescanning status table when accounts table changes. Recommended by Room(c).
    indices = [Index("authorServerId", "tayyarAccountId")]
)
@TypeConverters(Converters::class)
data class TimelineStatusEntity(
    // id never flips: we need it for sorting so it's a real id
    val serverId: String,
    val url: String?,
    // our local id for the logged in user in case there are multiple accounts per instance
    val tayyarAccountId: Long,
    val authorServerId: String,
    val inReplyToId: String?,
    val inReplyToAccountId: String?,
    val content: String,
    val createdAt: Long,
    val editedAt: Long?,
    val emojis: List<Emoji>,
    val reblogsCount: Int,
    val favouritesCount: Int,
    val repliesCount: Int,
    val reblogged: Boolean,
    val bookmarked: Boolean,
    val favourited: Boolean,
    val sensitive: Boolean,
    val spoilerText: String,
    val visibility: Status.Visibility,
    val attachments: List<Attachment>,
    val mentions: List<Status.Mention>,
    val tags: List<HashTag>,
    val application: Status.Application?,
    // if it has a reblogged status, it's id is stored here
    val poll: Poll?,
    val muted: Boolean,
    /** Also used as the "loading" attribute when this TimelineStatusEntity is a placeholder */
    val expanded: Boolean,
    val contentCollapsed: Boolean,
    val contentShowing: Boolean,
    val pinned: Boolean,
    val card: PreviewCard?,
    val language: String?,
    val filtered: List<FilterResult>
)
