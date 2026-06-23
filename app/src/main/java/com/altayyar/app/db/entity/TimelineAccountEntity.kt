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

package com.altayyar.app.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.altayyar.app.db.Converters
import com.altayyar.app.entity.Emoji

@Entity(
    primaryKeys = ["serverId", "tayyarAccountId"]
)
@TypeConverters(Converters::class)
data class TimelineAccountEntity(
    val serverId: String,
    val tayyarAccountId: Long,
    val localUsername: String,
    val username: String,
    val displayName: String,
    val url: String,
    val avatar: String,
    @ColumnInfo(defaultValue = "") val note: String,
    val emojis: List<Emoji>,
    val bot: Boolean
)
