package com.altayyar.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotificationPolicyEntity(
    @PrimaryKey val tayyarAccountId: Long,
    val pendingRequestsCount: Int,
    val pendingNotificationsCount: Int
)
