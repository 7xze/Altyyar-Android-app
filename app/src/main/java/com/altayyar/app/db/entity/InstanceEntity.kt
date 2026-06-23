/* Copyright 2018 Conny Duck
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
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.altayyar.app.db.Converters
import com.altayyar.app.entity.Emoji

@Entity
@TypeConverters(Converters::class)
data class InstanceEntity(
    @PrimaryKey val instance: String,
    val emojiList: List<Emoji>?,
    val maximumTootCharacters: Int?,
    val maxPollOptions: Int?,
    val maxPollOptionLength: Int?,
    val minPollDuration: Int?,
    val maxPollDuration: Int?,
    val charactersReservedPerUrl: Int?,
    val version: String?,
    val videoSizeLimit: Int?,
    val imageSizeLimit: Int?,
    val imageMatrixLimit: Int?,
    val maxMediaAttachments: Int?,
    val maxFields: Int?,
    val maxFieldNameLength: Int?,
    val maxFieldValueLength: Int?,
    val translationEnabled: Boolean?,
    val mastodonApiVersion: Int?,

    // ToDo: Remove this again when filter v1 support is dropped
    @ColumnInfo(defaultValue = "false") val filterV2Supported: Boolean = false
)

@TypeConverters(Converters::class)
data class EmojisEntity(
    @PrimaryKey val instance: String,
    val emojiList: List<Emoji>?
)

data class InstanceInfoEntity(
    @PrimaryKey val instance: String,
    val maximumTootCharacters: Int?,
    val maxPollOptions: Int?,
    val maxPollOptionLength: Int?,
    val minPollDuration: Int?,
    val maxPollDuration: Int?,
    val charactersReservedPerUrl: Int?,
    val version: String?,
    val videoSizeLimit: Int?,
    val imageSizeLimit: Int?,
    val imageMatrixLimit: Int?,
    val maxMediaAttachments: Int?,
    val maxFields: Int?,
    val maxFieldNameLength: Int?,
    val maxFieldValueLength: Int?,
    val translationEnabled: Boolean?,
    val mastodonApiVersion: Int?,
)
