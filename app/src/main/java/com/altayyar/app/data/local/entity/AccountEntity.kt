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

package com.altayyar.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.altayyar.app.presentation.state.TabData
import com.altayyar.app.presentation.ui.feature.systemnotifications.NotificationChannelData
import com.altayyar.app.data.local.Converters
import com.altayyar.app.presentation.state.defaultTabs
import com.altayyar.app.entity.Emoji
import com.altayyar.app.entity.Status
import com.altayyar.app.data.local.DefaultReplyVisibility

@Entity(
    indices = [
        Index(
            value = ["domain", "accountId"],
            unique = true
        )
    ]
)
@TypeConverters(Converters::class)
data class AccountEntity(
    @field:PrimaryKey(autoGenerate = true) val id: Long,
    val domain: String,
    val accessToken: String,
    // nullable for backward compatibility
    val clientId: String?,
    // nullable for backward compatibility
    val clientSecret: String?,
    val isActive: Boolean,
    val accountId: String = "",
    val username: String = "",
    val displayName: String = "",
    val profilePictureUrl: String = "",
    @ColumnInfo(defaultValue = "") val profileHeaderUrl: String = "",
    val notificationsEnabled: Boolean = true,
    val notificationsMentioned: Boolean = true,
    val notificationsFollowed: Boolean = true,
    val notificationsFollowRequested: Boolean = true,
    val notificationsReblogged: Boolean = true,
    val notificationsFavorited: Boolean = true,
    val notificationsPolls: Boolean = true,
    val notificationsSubscriptions: Boolean = true,
    val notificationsUpdates: Boolean = true,
    @ColumnInfo(defaultValue = "true") val notificationsAdmin: Boolean = true,
    @ColumnInfo(defaultValue = "true") val notificationsOther: Boolean = true,
    val notificationSound: Boolean = true,
    val notificationVibration: Boolean = true,
    val notificationLight: Boolean = true,
    val defaultPostPrivacy: Status.Visibility = Status.Visibility.PUBLIC,
    val defaultReplyPrivacy: DefaultReplyVisibility = DefaultReplyVisibility.MATCH_DEFAULT_POST_VISIBILITY,
    val defaultMediaSensitivity: Boolean = false,
    val defaultPostLanguage: String = "",
    val alwaysShowSensitiveMedia: Boolean = false,
    /** True if content behind a content warning is shown by default */
    @ColumnInfo(defaultValue = "0")
    val alwaysOpenSpoiler: Boolean = false,

    /**
     * True if the "Download media previews" preference is true. This implies
     * that media previews are shown as well as downloaded.
     */
    val mediaPreviewEnabled: Boolean = true,
    /**
     * ID of the last notification the user read on the Notification, list, and should be restored
     * to view when the user returns to the list.
     *
     * May not be the ID of the most recent notification if the user has scrolled down the list.
     */
    val lastNotificationId: String = "0",
    /**
     *  ID of the most recent Mastodon notification that Tayyar has fetched to show as an
     *  Android notification.
     */
    @ColumnInfo(defaultValue = "0")
    val notificationMarkerId: String = "0",
    val emojis: List<Emoji> = emptyList(),
    val tabPreferences: List<TabData> = defaultTabs(),
    val notificationsFilter: Set<NotificationChannelData> = emptySet(),
    // Scope cannot be changed without re-login, so store it in case
    // the scope needs to be changed in the future
    val oauthScopes: String = "",
    val unifiedPushUrl: String = "",
    val pushPubKey: String = "",
    val pushPrivKey: String = "",
    val pushAuth: String = "",
    val pushServerKey: String = "",

    /**
     * ID of the status at the top of the visible list in the home timeline when the
     * user navigated away.
     */
    val lastVisibleHomeTimelineStatusId: String? = null,

    /** true if the connected Mastodon account is locked (has to manually approve all follow requests **/
    @ColumnInfo(defaultValue = "0")
    val locked: Boolean = false,

    @ColumnInfo(defaultValue = "0")
    val hasDirectMessageBadge: Boolean = false,

    val isShowHomeBoosts: Boolean = true,
    val isShowHomeReplies: Boolean = true,
    val isShowHomeSelfBoosts: Boolean = true
) {
    val identifier: String
        get() = "$domain:$accountId"

    val fullName: String
        get() = "@$username@$domain"

    fun isPushNotificationsEnabled(): Boolean {
        return unifiedPushUrl.isNotEmpty()
    }

    fun matchesPushSubscription(endpoint: String): Boolean {
        return unifiedPushUrl == endpoint
    }
}
