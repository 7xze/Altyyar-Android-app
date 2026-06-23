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

package com.altayyar.app.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.altayyar.app.db.entity.HomeTimelineData
import com.altayyar.app.db.entity.HomeTimelineEntity

@Dao
abstract class TimelineDao {

    @Insert(onConflict = REPLACE)
    abstract suspend fun insertHomeTimelineItem(item: HomeTimelineEntity): Long

    @Query(
        """
SELECT h.id, s.serverId, s.url, s.tayyarAccountId,
s.authorServerId, s.inReplyToId, s.inReplyToAccountId, s.createdAt, s.editedAt,
s.emojis, s.reblogsCount, s.favouritesCount, s.repliesCount, s.reblogged, s.favourited, s.bookmarked, s.sensitive,
s.spoilerText, s.visibility, s.mentions, s.tags, s.application,
s.content, s.attachments, s.poll, s.card, s.muted, s.expanded, s.contentShowing, s.contentCollapsed, s.pinned, s.language, s.filtered,
a.serverId as 'a_serverId', a.tayyarAccountId as 'a_tayyarAccountId',
a.localUsername as 'a_localUsername', a.username as 'a_username',
a.displayName as 'a_displayName', a.url as 'a_url', a.avatar as 'a_avatar',
a.note as 'a_note', a.emojis as 'a_emojis', a.bot as 'a_bot',
rb.serverId as 'rb_serverId', rb.tayyarAccountId 'rb_tayyarAccountId',
rb.localUsername as 'rb_localUsername', rb.username as 'rb_username',
rb.displayName as 'rb_displayName', rb.url as 'rb_url', rb.avatar as 'rb_avatar',
rb.note as 'rb_note', rb.emojis as 'rb_emojis', rb.bot as 'rb_bot',
replied.serverId as 'replied_serverId', replied.tayyarAccountId 'replied_tayyarAccountId',
replied.localUsername as 'replied_localUsername', replied.username as 'replied_username',
replied.displayName as 'replied_displayName', replied.url as 'replied_url', replied.avatar as 'replied_avatar',
replied.note as 'replied_note', replied.emojis as 'replied_emojis', replied.bot as 'replied_bot',
h.loading
FROM HomeTimelineEntity h
LEFT JOIN TimelineStatusEntity s ON (h.statusId = s.serverId AND s.tayyarAccountId = :tayyarAccountId)
LEFT JOIN TimelineAccountEntity a ON (s.authorServerId = a.serverId AND a.tayyarAccountId = :tayyarAccountId)
LEFT JOIN TimelineAccountEntity rb ON (h.reblogAccountId = rb.serverId AND rb.tayyarAccountId = :tayyarAccountId)
LEFT JOIN TimelineAccountEntity replied ON (s.inReplyToAccountId = replied.serverId AND replied.tayyarAccountId = :tayyarAccountId)
WHERE h.tayyarAccountId = :tayyarAccountId
ORDER BY LENGTH(h.id) DESC, h.id DESC"""
    )
    abstract fun getHomeTimeline(tayyarAccountId: Long): PagingSource<Int, HomeTimelineData>

    @Query(
        """DELETE FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND
        (LENGTH(id) < LENGTH(:maxId) OR LENGTH(id) == LENGTH(:maxId) AND id <= :maxId)
AND
(LENGTH(id) > LENGTH(:minId) OR LENGTH(id) == LENGTH(:minId) AND id >= :minId)
    """
    )
    abstract suspend fun deleteRange(tayyarAccountId: Long, minId: String, maxId: String): Int

    /**
     * Remove all home timeline items that are statuses or reblogs by the user with id [userId], including reblogs from other people.
     * (e.g. because user was blocked)
     */
    @Query(
        """DELETE FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND
            (statusId IN
            (SELECT serverId FROM TimelineStatusEntity WHERE tayyarAccountId = :tayyarAccountId AND authorServerId == :userId)
            OR reblogAccountId == :userId)
        """
    )
    abstract suspend fun removeAllByUser(tayyarAccountId: Long, userId: String)

    /**
     * Remove all home timeline items that are statuses or reblogs by the user with id [userId], but not reblogs from other users.
     * (e.g. because user was unfollowed)
     */
    @Query(
        """DELETE FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND
            ((statusId IN
            (SELECT serverId FROM TimelineStatusEntity WHERE tayyarAccountId = :tayyarAccountId AND authorServerId == :userId)
            AND reblogAccountId IS NULL)
            OR reblogAccountId == :userId)
        """
    )
    abstract suspend fun removeStatusesAndReblogsByUser(tayyarAccountId: Long, userId: String)

    @Query("DELETE FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId")
    abstract suspend fun removeAllHomeTimelineItems(tayyarAccountId: Long)

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
     * Trims the HomeTimelineEntity table down to [limit] entries by deleting the oldest in case there are more than [limit].
     * @param tayyarAccountId id of the account for which to clean the home timeline
     * @param limit how many timeline items to keep
     */
    @Query(
        """DELETE FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId AND id NOT IN
        (SELECT id FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId ORDER BY LENGTH(id) DESC, id DESC LIMIT :limit)
    """
    )
    internal abstract suspend fun cleanupHomeTimeline(tayyarAccountId: Long, limit: Int)

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

    @Query("SELECT COUNT(*) FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId")
    abstract suspend fun getHomeTimelineItemCount(tayyarAccountId: Long): Int

    /** Developer tools: Find N most recent status IDs */
    @Query(
        "SELECT id FROM HomeTimelineEntity WHERE tayyarAccountId = :tayyarAccountId ORDER BY LENGTH(id) DESC, id DESC LIMIT :count"
    )
    abstract suspend fun getMostRecentNHomeTimelineIds(tayyarAccountId: Long, count: Int): List<String>

    /** Developer tools: Convert a home timeline item to a placeholder */
    @Query("UPDATE HomeTimelineEntity SET statusId = NULL, reblogAccountId = NULL WHERE id = :serverId")
    abstract suspend fun convertHomeTimelineItemToPlaceholder(serverId: String)
}
