/* Copyright 2020 Tayyar Contributors
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

package com.altayyar.app.presentation.ui.feature.announcements

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.connyduck.calladapter.networkresult.fold
import com.altayyar.app.presentation.AnnouncementReadEvent
import com.altayyar.app.presentation.EventHub
import com.altayyar.app.data.repository.InstanceInfoRepository
import com.altayyar.app.entity.Announcement
import com.altayyar.app.entity.Emoji
import com.altayyar.app.data.remote.MastodonApi
import com.altayyar.app.util.Error
import com.altayyar.app.util.Loading
import com.altayyar.app.util.Resource
import com.altayyar.app.util.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AnnouncementsViewModel @Inject constructor(
    private val instanceInfoRepo: InstanceInfoRepository,
    private val mastodonApi: MastodonApi,
    private val eventHub: EventHub
) : ViewModel() {

    private val _announcements = MutableStateFlow(null as Resource<List<Announcement>>?)
    val announcements: StateFlow<Resource<List<Announcement>>?> = _announcements.asStateFlow()

    private val _emoji = MutableStateFlow(emptyList<Emoji>())
    val emoji: StateFlow<List<Emoji>> = _emoji.asStateFlow()

    init {
        viewModelScope.launch {
            _emoji.value = instanceInfoRepo.getEmojis()
        }
    }

    fun load() {
        viewModelScope.launch {
            _announcements.value = Loading()
            mastodonApi.announcements()
                .fold(
                    {
                        _announcements.value = Success(it)
                        it.filter { announcement -> !announcement.read }
                            .forEach { announcement ->
                                mastodonApi.dismissAnnouncement(announcement.id)
                                    .fold(
                                        {
                                            eventHub.dispatch(
                                                AnnouncementReadEvent(announcement.id)
                                            )
                                        },
                                        { throwable ->
                                            Log.d(
                                                TAG,
                                                "Failed to mark announcement as read.",
                                                throwable
                                            )
                                        }
                                    )
                            }
                    },
                    {
                        _announcements.value = Error(cause = it)
                    }
                )
        }
    }

    fun addReaction(announcementId: String, name: String) {
        viewModelScope.launch {
            mastodonApi.addAnnouncementReaction(announcementId, name)
                .fold(
                    {
                        _announcements.value =
                            Success(
                                announcements.value?.data?.map { announcement ->
                                    if (announcement.id == announcementId) {
                                        announcement.copy(
                                            reactions = if (announcement.reactions.find { reaction -> reaction.name == name } != null) {
                                                announcement.reactions.map { reaction ->
                                                    if (reaction.name == name) {
                                                        reaction.copy(
                                                            count = reaction.count + 1,
                                                            me = true
                                                        )
                                                    } else {
                                                        reaction
                                                    }
                                                }
                                            } else {
                                                listOf(
                                                    *announcement.reactions.toTypedArray(),
                                                    emoji.value.find { emoji -> emoji.shortcode == name }!!.run {
                                                        Announcement.Reaction(
                                                            name,
                                                            1,
                                                            true,
                                                            url,
                                                            staticUrl
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    } else {
                                        announcement
                                    }
                                }
                            )
                    },
                    {
                        Log.w(TAG, "Failed to add reaction to the announcement.", it)
                    }
                )
        }
    }

    fun removeReaction(announcementId: String, name: String) {
        viewModelScope.launch {
            mastodonApi.removeAnnouncementReaction(announcementId, name)
                .fold(
                    {
                        _announcements.value =
                            Success(
                                announcements.value!!.data!!.map { announcement ->
                                    if (announcement.id == announcementId) {
                                        announcement.copy(
                                            reactions = announcement.reactions.mapNotNull { reaction ->
                                                if (reaction.name == name) {
                                                    if (reaction.count > 1) {
                                                        reaction.copy(
                                                            count = reaction.count - 1,
                                                            me = false
                                                        )
                                                    } else {
                                                        null
                                                    }
                                                } else {
                                                    reaction
                                                }
                                            }
                                        )
                                    } else {
                                        announcement
                                    }
                                }
                            )
                    },
                    {
                        Log.w(TAG, "Failed to remove reaction from the announcement.", it)
                    }
                )
        }
    }

    companion object {
        private const val TAG = "AnnouncementsViewModel"
    }
}
