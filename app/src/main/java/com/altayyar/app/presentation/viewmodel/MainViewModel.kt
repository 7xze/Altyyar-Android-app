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

package com.altayyar.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.connyduck.calladapter.networkresult.fold
import com.altayyar.app.presentation.AnnouncementReadEvent
import com.altayyar.app.presentation.ConversationsLoadingEvent
import com.altayyar.app.presentation.EventHub
import com.altayyar.app.presentation.NewNotificationsEvent
import com.altayyar.app.presentation.NotificationsLoadingEvent
import com.altayyar.app.presentation.state.TabData
import com.altayyar.app.presentation.ui.activity.MainActivity
import com.altayyar.app.presentation.ui.feature.systemnotifications.NotificationService
import com.altayyar.app.data.local.AccountManager
import com.altayyar.app.entity.Emoji
import com.altayyar.app.entity.Notification
import com.altayyar.app.entity.Status
import com.altayyar.app.data.remote.MastodonApi
import com.altayyar.app.util.ShareShortcutHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: MastodonApi,
    private val eventHub: EventHub,
    private val accountManager: AccountManager,
    private val shareShortcutHelper: ShareShortcutHelper,
    private val notificationService: NotificationService,
) : ViewModel() {

    private val activeAccount = accountManager.activeAccount!!

    val accounts: StateFlow<List<AccountViewData>> = accountManager.accountsFlow
        .map { accounts ->
            accounts.map { account ->
                AccountViewData(
                    id = account.id,
                    domain = account.domain,
                    username = account.username,
                    displayName = account.displayName,
                    profilePictureUrl = account.profilePictureUrl,
                    profileHeaderUrl = account.profileHeaderUrl,
                    emojis = account.emojis
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val tabs: StateFlow<List<TabData>> = accountManager.activeAccount(viewModelScope)
        .mapNotNull { account -> account?.tabPreferences }
        .stateIn(viewModelScope, SharingStarted.Eagerly, activeAccount.tabPreferences)

    private val _unreadAnnouncementsCount = MutableStateFlow(0)
    val unreadAnnouncementsCount: StateFlow<Int> = _unreadAnnouncementsCount.asStateFlow()

    val showDirectMessagesBadge: StateFlow<Boolean> = accountManager.activeAccount(viewModelScope)
        .map { account -> account?.hasDirectMessageBadge == true }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        loadAccountData()
        fetchAnnouncements()
        collectEvents()
    }

    private fun loadAccountData() {
        viewModelScope.launch {
            api.accountVerifyCredentials().fold(
                { userInfo ->
                    accountManager.updateAccount(activeAccount, userInfo)

                    shareShortcutHelper.updateShortcuts()
                },
                { throwable ->
                    Log.e(TAG, "Failed to fetch user info.", throwable)
                }
            )
        }
    }

    private fun fetchAnnouncements() {
        viewModelScope.launch {
            api.announcements()
                .fold(
                    { announcements ->
                        _unreadAnnouncementsCount.value = announcements.count { !it.read }
                    },
                    { throwable ->
                        Log.w(TAG, "Failed to fetch announcements.", throwable)
                    }
                )
        }
    }

    private fun collectEvents() {
        viewModelScope.launch {
            eventHub.events.collect { event ->
                when (event) {
                    is AnnouncementReadEvent -> {
                        _unreadAnnouncementsCount.value--
                    }
                    is NewNotificationsEvent -> {
                        if (event.accountId == activeAccount.accountId) {
                            val hasDirectMessageNotification =
                                event.notifications.any {
                                    it.type == Notification.Type.Mention && it.status?.visibility == Status.Visibility.DIRECT
                                }

                            if (hasDirectMessageNotification) {
                                accountManager.updateAccount(activeAccount) { copy(hasDirectMessageBadge = true) }
                            }
                        }
                    }
                    is NotificationsLoadingEvent -> {
                        if (event.accountId == activeAccount.accountId) {
                            accountManager.updateAccount(activeAccount) { copy(hasDirectMessageBadge = false) }
                        }
                    }
                    is ConversationsLoadingEvent -> {
                        if (event.accountId == activeAccount.accountId) {
                            accountManager.updateAccount(activeAccount) { copy(hasDirectMessageBadge = false) }
                        }
                    }
                }
            }
        }
    }

    fun dismissDirectMessagesBadge() {
        viewModelScope.launch {
            accountManager.updateAccount(activeAccount) { copy(hasDirectMessageBadge = false) }
        }
    }

    fun setupNotifications(activity: MainActivity) {
        // TODO this is only called on full app (re) start; so changes in-between (push distributor uninstalled/subscription changed, or
        //   notifications fully disabled) will get unnoticed; and also an app restart cannot be easily triggered by the user.

        // TODO it's quite odd to separate channel creation (for an account) from the "is enabled by channels" question below
        notificationService.createNotificationChannelsForAccount(activeAccount)

        if (notificationService.areNotificationsEnabledBySystem()) {
            viewModelScope.launch {
                notificationService.setupNotifications(activity)
            }
        } else {
            viewModelScope.launch {
                notificationService.disableAllNotifications()
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}

data class AccountViewData(
    val id: Long,
    val domain: String,
    val username: String,
    val displayName: String,
    val profilePictureUrl: String,
    val profileHeaderUrl: String,
    val emojis: List<Emoji>
) {
    val fullName: String
        get() = "@$username@$domain"
}
