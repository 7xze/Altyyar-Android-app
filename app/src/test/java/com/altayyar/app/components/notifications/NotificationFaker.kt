package com.altayyar.app.components.notifications

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.altayyar.app.components.timeline.LoadMorePlaceholder
import com.altayyar.app.components.timeline.fakeAccount
import com.altayyar.app.components.timeline.fakeStatus
import com.altayyar.app.components.timeline.toEntity
import com.altayyar.app.db.AppDatabase
import com.altayyar.app.db.entity.NotificationDataEntity
import com.altayyar.app.db.entity.NotificationEntity
import com.altayyar.app.entity.Notification
import com.altayyar.app.entity.Report
import com.altayyar.app.entity.Status
import com.altayyar.app.entity.TimelineAccount
import java.util.Date
import org.junit.Assert.assertEquals

fun fakeNotification(
    type: Notification.Type = Notification.Type.Favourite,
    id: String = "1",
    account: TimelineAccount = fakeAccount(id = id),
    status: Status? = fakeStatus(id = id),
    report: Report? = null
) = Notification(
    type = type,
    id = id,
    account = account,
    status = status,
    report = report
)

fun fakeReport(
    id: String = "1",
    category: String = "spam",
    statusIds: List<String>? = null,
    createdAt: Date = Date(1712509983273),
    targetAccount: TimelineAccount = fakeAccount()
) = Report(
    id = id,
    category = category,
    statusIds = statusIds,
    createdAt = createdAt,
    targetAccount = targetAccount
)

fun Notification.toNotificationDataEntity(
    tayyarAccountId: Long,
    isStatusExpanded: Boolean = false,
    isStatusContentShowing: Boolean = false
) = NotificationDataEntity(
    tayyarAccountId = tayyarAccountId,
    type = type,
    id = id,
    account = account.toEntity(tayyarAccountId),
    status = status?.toEntity(
        tayyarAccountId = tayyarAccountId,
        expanded = isStatusExpanded,
        contentShowing = isStatusContentShowing,
        contentCollapsed = true
    ),
    statusAccount = status?.account?.toEntity(tayyarAccountId),
    report = report?.toEntity(tayyarAccountId),
    reportTargetAccount = report?.targetAccount?.toEntity(tayyarAccountId),
    event = null,
    moderationWarning = null,
)

fun LoadMorePlaceholder.toNotificationDataEntity(
    tayyarAccountId: Long
) = NotificationDataEntity(
    tayyarAccountId = tayyarAccountId,
    type = null,
    id = id,
    account = null,
    status = null,
    statusAccount = null,
    report = null,
    reportTargetAccount = null,
    event = null,
    moderationWarning = null,
)

suspend fun AppDatabase.insert(notifications: List<Notification>, tayyarAccountId: Long = 1) = withTransaction {
    notifications.forEach { notification ->

        timelineAccountDao().insert(
            notification.account.toEntity(tayyarAccountId)
        )

        notification.report?.let { report ->
            timelineAccountDao().insert(
                report.targetAccount.toEntity(
                    tayyarAccountId = tayyarAccountId,
                )
            )
            notificationsDao().insertReport(report.toEntity(tayyarAccountId))
        }
        notification.status?.let { status ->
            timelineAccountDao().insert(
                status.account.toEntity(
                    tayyarAccountId = tayyarAccountId,
                )
            )
            timelineStatusDao().insert(
                status.toEntity(
                    tayyarAccountId = tayyarAccountId,
                    expanded = false,
                    contentShowing = false,
                    contentCollapsed = true
                )
            )
        }
        notificationsDao().insertNotification(
            NotificationEntity(
                tayyarAccountId = tayyarAccountId,
                type = notification.type,
                id = notification.id,
                accountId = notification.account.id,
                statusId = notification.status?.id,
                reportId = notification.report?.id,
                event = null,
                moderationWarning = null,
                loading = false
            )
        )
    }
}

suspend fun AppDatabase.assertNotifications(
    expected: List<NotificationDataEntity>,
    tayyarAccountId: Long = 1
) {
    val pagingSource = notificationsDao().getNotifications(tayyarAccountId)

    val loadResult = pagingSource.load(PagingSource.LoadParams.Refresh(null, 100, false))

    val loaded = (loadResult as PagingSource.LoadResult.Page).data

    assertEquals(expected, loaded)
}
