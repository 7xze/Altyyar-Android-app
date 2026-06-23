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

package com.altayyar.app.components.notifications

import com.altayyar.app.components.timeline.LoadMorePlaceholder
import com.altayyar.app.components.timeline.toAccount
import com.altayyar.app.components.timeline.toStatus
import com.altayyar.app.db.entity.NotificationDataEntity
import com.altayyar.app.db.entity.NotificationEntity
import com.altayyar.app.db.entity.NotificationReportEntity
import com.altayyar.app.db.entity.TimelineAccountEntity
import com.altayyar.app.entity.Filter
import com.altayyar.app.entity.Notification
import com.altayyar.app.entity.Report
import com.altayyar.app.util.toViewData
import com.altayyar.app.viewdata.NotificationViewData
import com.altayyar.app.viewdata.StatusViewData
import com.altayyar.app.viewdata.TranslationViewData

fun LoadMorePlaceholder.toNotificationEntity(
    tayyarAccountId: Long
) = NotificationEntity(
    id = this.id,
    tayyarAccountId = tayyarAccountId,
    type = null,
    accountId = null,
    statusId = null,
    reportId = null,
    event = null,
    moderationWarning = null,
    loading = loading
)

fun Notification.toEntity(
    tayyarAccountId: Long
) = NotificationEntity(
    tayyarAccountId = tayyarAccountId,
    type = type,
    id = id,
    accountId = account.id,
    statusId = status?.reblog?.id ?: status?.id,
    reportId = report?.id,
    event = event,
    moderationWarning = moderationWarning,
    loading = false
)

fun Notification.toViewData(
    isShowingContent: Boolean,
    isExpanded: Boolean,
    isCollapsed: Boolean,
    filter: Filter?,
): NotificationViewData.Concrete = NotificationViewData.Concrete(
    id = id,
    type = type,
    account = account,
    statusViewData = status?.toViewData(
        isShowingContent = isShowingContent,
        isExpanded = isExpanded,
        isCollapsed = isCollapsed,
        filter = filter,
    ),
    report = report,
    moderationWarning = moderationWarning,
    event = event
)

fun Report.toEntity(
    tayyarAccountId: Long
) = NotificationReportEntity(
    tayyarAccountId = tayyarAccountId,
    serverId = id,
    category = category,
    statusIds = statusIds,
    createdAt = createdAt,
    targetAccountId = targetAccount.id
)

fun NotificationDataEntity.toViewData(
    translation: TranslationViewData? = null
): NotificationViewData {
    if (type == null || account == null) {
        return NotificationViewData.LoadMore(id = id, isLoading = loading)
    }

    return NotificationViewData.Concrete(
        id = id,
        type = type,
        account = account.toAccount(),
        statusViewData = if (status != null && statusAccount != null) {
            StatusViewData.Concrete(
                status = status.toStatus(statusAccount),
                isExpanded = this.status.expanded,
                isShowingContent = this.status.contentShowing,
                isCollapsed = this.status.contentCollapsed,
                translation = translation
            )
        } else {
            null
        },
        report = if (report != null && reportTargetAccount != null) {
            report.toReport(reportTargetAccount)
        } else {
            null
        },
        event = event,
        moderationWarning = moderationWarning
    )
}

fun NotificationReportEntity.toReport(
    account: TimelineAccountEntity
) = Report(
    id = serverId,
    category = category,
    statusIds = statusIds,
    createdAt = createdAt,
    targetAccount = account.toAccount()
)
