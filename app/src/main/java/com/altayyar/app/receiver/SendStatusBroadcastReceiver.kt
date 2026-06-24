/* Copyright Tayyar Contributors
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

package com.altayyar.app.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.altayyar.app.R
import com.altayyar.app.presentation.ui.feature.systemnotifications.NotificationChannelData
import com.altayyar.app.presentation.ui.feature.systemnotifications.NotificationService
import com.altayyar.app.data.local.AccountManager
import com.altayyar.app.entity.Status
import com.altayyar.app.service.SendStatusService
import com.altayyar.app.service.StatusToSend
import com.altayyar.app.util.getSerializableExtraCompat
import com.altayyar.app.util.randomAlphanumericString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SendStatusBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var accountManager: AccountManager

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == NotificationService.REPLY_ACTION) {
            val serverNotificationId = intent.getStringExtra(NotificationService.KEY_SERVER_NOTIFICATION_ID)
            val senderId = intent.getLongExtra(NotificationService.KEY_SENDER_ACCOUNT_ID, -1)
            val senderIdentifier = intent.getStringExtra(
                NotificationService.KEY_SENDER_ACCOUNT_IDENTIFIER
            )!!
            val senderFullName = intent.getStringExtra(
                NotificationService.KEY_SENDER_ACCOUNT_FULL_NAME
            )
            val citedStatusId = intent.getStringExtra(NotificationService.KEY_CITED_STATUS_ID)
            val visibility =
                intent.getSerializableExtraCompat<Status.Visibility>(NotificationService.KEY_VISIBILITY)!!
            val spoiler = intent.getStringExtra(NotificationService.KEY_SPOILER).orEmpty()
            val mentions = intent.getStringArrayExtra(NotificationService.KEY_MENTIONS).orEmpty()

            val account = accountManager.getAccountById(senderId)

            val notificationManager = NotificationManagerCompat.from(context)

            val message = getReplyMessage(intent)

            if (account == null) {
                Log.w(TAG, "Account \"$senderId\" not found in database. Aborting quick reply!")

                val notification = NotificationCompat.Builder(
                    context,
                    NotificationChannelData.MENTION.getChannelId(senderIdentifier)
                )
                    .setSmallIcon(R.drawable.tayyar_notification_icon)
                    .setColor(context.getColor(R.color.tayyar_blue))
                    .setGroup(senderFullName)
                    .setDefaults(0) // We don't want this to make any sound or vibration
                    .setOnlyAlertOnce(true)
                    .setContentTitle(context.getString(R.string.error_generic))
                    .setContentText(context.getString(R.string.error_sender_account_gone))
                    .setSubText(senderFullName)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                    .build()

                notificationManager.notify(serverNotificationId, senderId.toInt(), notification)
            } else {
                val text = mentions.joinToString(" ", postfix = " ") { "@$it" } + message.toString()

                val sendIntent = SendStatusService.sendStatusIntent(
                    context,
                    StatusToSend(
                        text = text,
                        warningText = spoiler,
                        visibility = visibility.stringValue,
                        sensitive = false,
                        media = emptyList(),
                        scheduledAt = null,
                        inReplyToId = citedStatusId,
                        poll = null,
                        replyingStatusContent = null,
                        replyingStatusAuthorUsername = null,
                        accountId = account.id,
                        idempotencyKey = randomAlphanumericString(16),
                        retries = 0,
                        language = null,
                        statusId = null
                    )
                )

                context.startService(sendIntent)

                // Notifications with remote input active can't be cancelled, so let's replace it with another one that will dismiss automatically
                val notification = NotificationCompat.Builder(
                    context,
                    NotificationChannelData.MENTION.getChannelId(senderIdentifier)
                )
                    .setSmallIcon(R.drawable.tayyar_notification_icon)
                    .setColor(context.getColor(R.color.notification_color))
                    .setGroup(senderFullName)
                    .setDefaults(0) // We don't want this to make any sound or vibration
                    .setOnlyAlertOnce(true)
                    .setContentTitle(context.getString(R.string.reply_sending))
                    .setContentText(context.getString(R.string.reply_sending_long))
                    .setSubText(senderFullName)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setCategory(NotificationCompat.CATEGORY_SOCIAL)
                    .setTimeoutAfter(5000)
                    .build()

                notificationManager.notify(serverNotificationId, senderId.toInt(), notification)
            }
        }
    }

    private fun getReplyMessage(intent: Intent): CharSequence {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        return remoteInput?.getCharSequence(NotificationService.KEY_REPLY, "") ?: ""
    }

    companion object {
        const val TAG = "SendStatusBroadcastReceiver"
    }
}
