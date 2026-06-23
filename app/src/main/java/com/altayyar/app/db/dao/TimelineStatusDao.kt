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

package com.altayyar.app.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import com.altayyar.app.db.AppDatabase
import com.altayyar.app.db.Converters
import com.altayyar.app.db.entity.TimelineAccountEntity
import com.altayyar.app.db.entity.TimelineStatusEntity
import com.altayyar.app.entity.Attachment
import com.altayyar.app.entity.Emoji
import com.altayyar.app.entity.HashTag
import com.altayyar.app.entity.Poll
import com.altayyar.app.entity.PreviewCard
import com.altayyar.app.entity.Status

@Dao
abstract class TimelineStatusDao(
    private val db: AppDatabase
) {

    @Insert(onConflict = REPLACE)
    abstract suspend fun insert(timelineStatusEntity: TimelineStatusEntity): Long

    @Transaction
    open suspend fun getStatusWithAccount(tayyarAccountId: Long, statusId: String): Pair<TimelineStatusEntity, TimelineAccountEntity>? {
        val status = getStatus(tayyarAccountId, statusId) ?: return null
        val account = db.timelineAccountDao().getAccount(tayyarAccountId, status.authorServerId) ?: return null
        return status to account
    }

    @Query(
        """
SELECT * FROM TimelineStatusEntity s
WHERE s.serverId = :statusId
AND s.authorServerId IS NOT NULL
AND s.tayyarAccountId = :tayyarAccountId"""
    )
    abstract suspend fun getStatus(tayyarAccountId: Long, statusId: String): TimelineStatusEntity?

    suspend fun update(tayyarAccountId: Long, status: Status) {
        update(
            tayyarAccountId = tayyarAccountId,
            statusId = status.id,
            content = status.content,
            editedAt = status.editedAt?.time,
            emojis = status.emojis,
            reblogsCount = status.reblogsCount,
            favouritesCount = status.favouritesCount,
            repliesCount = status.repliesCount,
            reblogged = status.reblogged,
            bookmarked = status.bookmarked,
            favourited = status.favourited,
            sensitive = status.sensitive,
            spoilerText = status.spoilerText,
            visibility = status.visibility,
            attachments = status.attachments,
            mentions = status.mentions,
            tags = status.tags,
            poll = status.poll,
            muted = status.muted,
            pinned = status.pinned,
            card = status.card,
            language = status.language
        )
    }

    @Query(
        """UPDATE TimelineStatusEntity
           SET content = :content,
           editedAt = :editedAt,
           emojis = :emojis,
           reblogsCount = :reblogsCount,
           favouritesCount = :favouritesCount,
           repliesCount = :repliesCount,
           reblogged = :reblogged,
           bookmarked = :bookmarked,
           favourited = :favourited,
           sensitive = :sensitive,
           spoilerText = :spoilerText,
           visibility = :visibility,
           attachments = :attachments,
           mentions = :mentions,
           tags = :tags,
           poll = :poll,
           muted = :muted,
           pinned = :pinned,
           card = :card,
           language = :language
           WHERE tayyarAccountId = :tayyarAccountId AND serverId = :statusId"""
    )
    @TypeConverters(Converters::class)
    abstract suspend fun update(
        tayyarAccountId: Long,
        statusId: String,
        content: String?,
        editedAt: Long?,
        emojis: List<Emoji>?,
        reblogsCount: Int,
        favouritesCount: Int,
        repliesCount: Int,
        reblogged: Boolean,
        bookmarked: Boolean,
        favourited: Boolean,
        sensitive: Boolean,
        spoilerText: String,
        visibility: Status.Visibility,
        attachments: List<Attachment>?,
        mentions: List<Status.Mention>?,
        tags: List<HashTag>?,
        poll: Poll?,
        muted: Boolean?,
        pinned: Boolean,
        card: PreviewCard?,
        language: String?
    )

    @Query(
        """UPDATE TimelineStatusEntity SET bookmarked = :bookmarked
WHERE tayyarAccountId = :tayyarAccountId AND serverId = :statusId"""
    )
    abstract suspend fun setBookmarked(tayyarAccountId: Long, statusId: String, bookmarked: Boolean)

    @Query(
        """UPDATE TimelineStatusEntity SET reblogged = :reblogged
WHERE tayyarAccountId = :tayyarAccountId AND serverId = :statusId"""
    )
    abstract suspend fun setReblogged(tayyarAccountId: Long, statusId: String, reblogged: Boolean)

    @Query("DELETE FROM TimelineStatusEntity WHERE tayyarAccountId = :tayyarAccountId")
    abstract suspend fun removeAllStatuses(tayyarAccountId: Long)

    @Query(
        """DELETE FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND id = :id"""
    )
    abstract suspend fun deleteHomeTimelineItem(tayyarAccountId: Long, id: String)

    /**
     * Deletes all hometimeline items that reference the status with it [statusId]. They can be regular statuses or reblogs.
     */
    @Query(
        """DELETE FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND statusId = :statusId"""
    )
    abstract suspend fun deleteAllWithStatus(tayyarAccountId: Long, statusId: String)

    /**
     * Cleans the TimelineStatusEntity table from unreferenced status entries.
     * @param tayyarAccountId id of the account for which to clean statuses
     */
    @Query(
        """DELETE FROM TimelineStatusEntity WHERE tayyarAccountId = :tayyarAccountId
        AND serverId NOT IN
        (SELECT statusId FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND statusId IS NOT NULL)
        AND serverId NOT IN
        (SELECT statusId FROM NotificationEntity WHERE tayyarAccountId = :tayyarAccountId AND statusId IS NOT NULL)"""
    )
    internal abstract suspend fun cleanupStatuses(tayyarAccountId: Long)

    @Query(
        """UPDATE TimelineStatusEntity SET poll = :poll
WHERE tayyarAccountId = :tayyarAccountId AND serverId = :statusId"""
    )
    @TypeConverters(Converters::class)
    abstract suspend fun setVoted(tayyarAccountId: Long, statusId: String, poll: Poll)

    @Transaction
    open suspend fun setShowResults(tayyarAccountId: Long, statusId: String) {
        getStatus(tayyarAccountId, statusId)?.let { status ->
            status.poll?.let { poll ->
                setVoted(tayyarAccountId, statusId, poll.copy(voted = true))
            }
        }
    }

    @Query(
        """UPDATE TimelineStatusEntity SET expanded = :expanded
WHERE tayyarAccountId = :tayyarAccountId AND serverId = :statusId"""
    )
    abstract suspend fun setExpanded(tayyarAccountId: Long, statusId: String, expanded: Boolean)

    @Query(
        """UPDATE TimelineStatusEntity SET contentShowing = :contentShowing
WHERE tayyarAccountId = :tayyarAccountId AND serverId = :statusId"""
    )
    abstract suspend fun setContentShowing(
        tayyarAccountId: Long,
        statusId: String,
        contentShowing: Boolean
    )

    @Query(
        """UPDATE TimelineStatusEntity SET contentCollapsed = :contentCollapsed
WHERE tayyarAccountId = :tayyarAccountId AND serverId = :statusId"""
    )
    abstract suspend fun setContentCollapsed(
        tayyarAccountId: Long,
        statusId: String,
        contentCollapsed: Boolean
    )

    @Query(
        """UPDATE TimelineStatusEntity SET pinned = :pinned
WHERE tayyarAccountId = :tayyarAccountId AND serverId = :statusId"""
    )
    abstract suspend fun setPinned(tayyarAccountId: Long, statusId: String, pinned: Boolean)

    @Query(
        """DELETE FROM HomeTimelineEntity
WHERE tayyarAccountId = :tayyarAccountId AND statusId IN (
SELECT serverId FROM TimelineStatusEntity WHERE tayyarAccountId = :tayyarAccountId AND authorServerId in
( SELECT serverId FROM TimelineAccountEntity WHERE username LIKE '%@' || :instanceDomain
AND tayyarAccountId = :tayyarAccountId
))"""
    )
    abstract suspend fun deleteAllFromInstance(tayyarAccountId: Long, instanceDomain: String)

    @Query(
        "UPDATE TimelineStatusEntity SET filtered = '[]' WHERE tayyarAccountId = :tayyarAccountId AND serverId = :statusId"
    )
    abstract suspend fun clearWarning(tayyarAccountId: Long, statusId: String): Int

    @Query(
        "SELECT id FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId ORDER BY LENGTH(id) DESC, id DESC LIMIT 1"
    )
    abstract suspend fun getTopId(tayyarAccountId: Long): String?

    @Query(
        "SELECT id FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND statusId IS NULL ORDER BY LENGTH(id) DESC, id DESC LIMIT 1"
    )
    abstract suspend fun getTopPlaceholderId(tayyarAccountId: Long): String?

    /**
     * Returns the id directly above [id], or null if [id] is the id of the top item
     */
    @Query(
        "SELECT id FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND (LENGTH(:id) < LENGTH(id) OR (LENGTH(:id) = LENGTH(id) AND :id < id)) ORDER BY LENGTH(id) ASC, id ASC LIMIT 1"
    )
    abstract suspend fun getIdAbove(tayyarAccountId: Long, id: String): String?

    /**
     * Returns the ID directly below [id], or null if [id] is the ID of the bottom item
     */
    @Query(
        "SELECT id FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND (LENGTH(:id) > LENGTH(id) OR (LENGTH(:id) = LENGTH(id) AND :id > id)) ORDER BY LENGTH(id) DESC, id DESC LIMIT 1"
    )
    abstract suspend fun getIdBelow(tayyarAccountId: Long, id: String): String?

    /**
     * Returns the id of the next placeholder after [id], or null if there is no placeholder.
     */
    @Query(
        "SELECT id FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND statusId IS NULL AND (LENGTH(:id) > LENGTH(id) OR (LENGTH(:id) = LENGTH(id) AND :id > id)) ORDER BY LENGTH(id) DESC, id DESC LIMIT 1"
    )
    abstract suspend fun getNextPlaceholderIdAfter(tayyarAccountId: Long, id: String): String?

    @Query("SELECT COUNT(*) FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId")
    abstract suspend fun getHomeTimelineItemCount(tayyarAccountId: Long): Int

    /** Developer tools: Find N most recent status IDs */
    @Query(
        "SELECT id FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId ORDER BY LENGTH(id) DESC, id DESC LIMIT :count"
    )
    abstract suspend fun getMostRecentNStatusIds(tayyarAccountId: Long, count: Int): List<String>

    /** Developer tools: Convert a home timeline item to a placeholder */
    @Query("UPDATE HomeTimelineEntity SET statusId = NULL, reblogAccountId = NULL WHERE id = :serverId")
    abstract suspend fun convertStatusToPlaceholder(serverId: String)
}
