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

package com.altayyar.app.presentation.ui.feature.timeline

import com.altayyar.app.data.local.entity.HomeTimelineData
import com.altayyar.app.data.local.entity.HomeTimelineEntity
import com.altayyar.app.data.local.entity.TimelineAccountEntity
import com.altayyar.app.data.local.entity.TimelineStatusEntity
import com.altayyar.app.entity.Status
import com.altayyar.app.entity.TimelineAccount
import com.altayyar.app.presentation.state.StatusViewData
import com.altayyar.app.presentation.state.TranslationViewData
import java.util.Date

data class LoadMorePlaceholder(
    val id: String,
    val loading: Boolean
)

fun TimelineAccount.toEntity(tayyarAccountId: Long): TimelineAccountEntity {
    return TimelineAccountEntity(
        serverId = id,
        tayyarAccountId = tayyarAccountId,
        localUsername = localUsername,
        username = username,
        displayName = name,
        url = url,
        avatar = avatar,
        emojis = emojis,
        note = note,
        bot = bot
    )
}

fun TimelineAccountEntity.toAccount(): TimelineAccount {
    return TimelineAccount(
        id = serverId,
        localUsername = localUsername,
        username = username,
        displayName = displayName,
        note = note,
        url = url,
        avatar = avatar,
        bot = bot,
        emojis = emojis
    )
}

fun LoadMorePlaceholder.toEntity(tayyarAccountId: Long): HomeTimelineEntity {
    return HomeTimelineEntity(
        id = this.id,
        tayyarAccountId = tayyarAccountId,
        statusId = null,
        reblogAccountId = null,
        loading = this.loading
    )
}

fun Status.toEntity(
    tayyarAccountId: Long,
    expanded: Boolean,
    contentShowing: Boolean,
    contentCollapsed: Boolean
) = TimelineStatusEntity(
    serverId = id,
    url = actionableStatus.url,
    tayyarAccountId = tayyarAccountId,
    authorServerId = actionableStatus.account.id,
    inReplyToId = actionableStatus.inReplyToId,
    inReplyToAccountId = actionableStatus.inReplyToAccountId,
    content = actionableStatus.content,
    createdAt = actionableStatus.createdAt.time,
    editedAt = actionableStatus.editedAt?.time,
    emojis = actionableStatus.emojis,
    reblogsCount = actionableStatus.reblogsCount,
    favouritesCount = actionableStatus.favouritesCount,
    reblogged = actionableStatus.reblogged,
    favourited = actionableStatus.favourited,
    bookmarked = actionableStatus.bookmarked,
    sensitive = actionableStatus.sensitive,
    spoilerText = actionableStatus.spoilerText,
    visibility = actionableStatus.visibility,
    attachments = actionableStatus.attachments,
    mentions = actionableStatus.mentions,
    tags = actionableStatus.tags,
    application = actionableStatus.application,
    poll = actionableStatus.poll,
    muted = actionableStatus.muted,
    expanded = expanded,
    contentShowing = contentShowing,
    contentCollapsed = contentCollapsed,
    pinned = actionableStatus.pinned,
    card = actionableStatus.card,
    repliesCount = actionableStatus.repliesCount,
    language = actionableStatus.language,
    filtered = actionableStatus.filtered.orEmpty()
)

fun TimelineStatusEntity.toStatus(
    account: TimelineAccountEntity,
) = Status(
    id = serverId,
    url = url,
    account = account.toAccount(),
    inReplyToId = inReplyToId,
    inReplyToAccountId = inReplyToAccountId,
    reblog = null,
    content = content,
    createdAt = Date(createdAt),
    editedAt = editedAt?.let { Date(it) },
    emojis = emojis,
    reblogsCount = reblogsCount,
    favouritesCount = favouritesCount,
    reblogged = reblogged,
    favourited = favourited,
    bookmarked = bookmarked,
    sensitive = sensitive,
    spoilerText = spoilerText,
    visibility = visibility,
    attachments = attachments,
    mentions = mentions,
    tags = tags,
    application = application,
    pinned = false,
    muted = muted,
    poll = poll,
    card = card,
    repliesCount = repliesCount,
    language = language,
    filtered = filtered,
)

fun HomeTimelineData.toViewData(
    isDetailed: Boolean = false,
    translation: TranslationViewData? = null,
): StatusViewData {
    if (this.account == null || this.status == null) {
        return StatusViewData.LoadMore(this.id, loading)
    }

    val originalStatus = status.toStatus(account)
    val status = if (reblogAccount != null) {
        Status(
            id = id,
            // no url for reblogs
            url = null,
            account = reblogAccount.toAccount(),
            inReplyToId = status.inReplyToId,
            inReplyToAccountId = status.inReplyToAccountId,
            reblog = originalStatus,
            content = status.content,
            // lie but whatever?
            createdAt = Date(status.createdAt),
            editedAt = null,
            emojis = emptyList(),
            reblogsCount = status.reblogsCount,
            favouritesCount = status.favouritesCount,
            reblogged = status.reblogged,
            favourited = status.favourited,
            bookmarked = status.bookmarked,
            sensitive = status.sensitive,
            spoilerText = status.spoilerText,
            visibility = status.visibility,
            attachments = emptyList(),
            mentions = emptyList(),
            tags = emptyList(),
            application = null,
            pinned = false,
            muted = status.muted,
            poll = null,
            card = null,
            repliesCount = status.repliesCount,
            language = status.language,
            filtered = status.filtered,
        )
    } else {
        originalStatus
    }

    return StatusViewData.Concrete(
        status = status,
        isExpanded = this.status.expanded,
        isShowingContent = this.status.contentShowing,
        isCollapsed = this.status.contentCollapsed,
        isDetailed = isDetailed,
        repliedToAccount = repliedToAccount?.toAccount(),
        translation = translation,
    )
}
