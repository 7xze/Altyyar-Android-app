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

package com.altayyar.app.presentation.ui.feature.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.presentation.ui.adapter.FilteredStatusViewHolder
import com.altayyar.app.presentation.ui.adapter.FollowRequestViewHolder
import com.altayyar.app.presentation.ui.adapter.LoadMoreViewHolder
import com.altayyar.app.presentation.ui.adapter.PlaceholderViewHolder
import com.altayyar.app.presentation.ui.adapter.StatusBaseViewHolder
import com.altayyar.app.databinding.ItemFollowBinding
import com.altayyar.app.databinding.ItemFollowRequestBinding
import com.altayyar.app.databinding.ItemLoadMoreBinding
import com.altayyar.app.databinding.ItemModerationWarningNotificationBinding
import com.altayyar.app.databinding.ItemPlaceholderBinding
import com.altayyar.app.databinding.ItemReportNotificationBinding
import com.altayyar.app.databinding.ItemSeveredRelationshipNotificationBinding
import com.altayyar.app.databinding.ItemStatusFilteredBinding
import com.altayyar.app.databinding.ItemStatusNotificationBinding
import com.altayyar.app.databinding.ItemUnknownNotificationBinding
import com.altayyar.app.entity.Filter
import com.altayyar.app.entity.Notification
import com.altayyar.app.domain.repository.AccountActionListener
import com.altayyar.app.domain.repository.StatusActionListener
import com.altayyar.app.util.AbsoluteTimeFormatter
import com.altayyar.app.util.StatusDisplayOptions
import com.altayyar.app.presentation.state.NotificationViewData

interface NotificationActionListener {
    fun onViewReport(reportId: String)
}

interface NotificationsViewHolder {
    fun bind(
        viewData: NotificationViewData.Concrete,
        payloads: List<*>,
        statusDisplayOptions: StatusDisplayOptions
    )
}

class NotificationsPagingAdapter(
    private val accountId: String,
    private var statusDisplayOptions: StatusDisplayOptions,
    private val statusListener: StatusActionListener,
    private val notificationActionListener: NotificationActionListener,
    private val accountActionListener: AccountActionListener,
    private val instanceName: String
) : PagingDataAdapter<NotificationViewData, RecyclerView.ViewHolder>(NotificationsDifferCallback) {

    var mediaPreviewEnabled: Boolean
        get() = statusDisplayOptions.mediaPreviewEnabled
        set(mediaPreviewEnabled) {
            statusDisplayOptions = statusDisplayOptions.copy(
                mediaPreviewEnabled = mediaPreviewEnabled
            )
            notifyItemRangeChanged(0, itemCount)
        }

    private val absoluteTimeFormatter = AbsoluteTimeFormatter()

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun getItemViewType(position: Int): Int {
        return when (val notification = getItem(position)) {
            is NotificationViewData.LoadMore -> VIEW_TYPE_LOAD_MORE
            is NotificationViewData.Concrete -> {
                when (notification.type) {
                    Notification.Type.Mention,
                    Notification.Type.Poll -> if (notification.statusViewData?.filter?.action == Filter.Action.WARN) {
                        VIEW_TYPE_STATUS_FILTERED
                    } else {
                        VIEW_TYPE_STATUS
                    }
                    Notification.Type.Status,
                    Notification.Type.Update -> if (notification.statusViewData?.filter?.action == Filter.Action.WARN) {
                        VIEW_TYPE_STATUS_FILTERED
                    } else {
                        VIEW_TYPE_STATUS_NOTIFICATION
                    }
                    Notification.Type.Favourite,
                    Notification.Type.Reblog -> VIEW_TYPE_STATUS_NOTIFICATION
                    Notification.Type.Follow,
                    Notification.Type.SignUp -> VIEW_TYPE_FOLLOW
                    Notification.Type.FollowRequest -> VIEW_TYPE_FOLLOW_REQUEST
                    Notification.Type.Report -> VIEW_TYPE_REPORT
                    Notification.Type.SeveredRelationship -> VIEW_TYPE_SEVERED_RELATIONSHIP
                    Notification.Type.ModerationWarning -> VIEW_TYPE_MODERATION_WARNING
                    else -> VIEW_TYPE_UNKNOWN
                }
            }
            null -> VIEW_TYPE_PLACEHOLDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_PLACEHOLDER -> PlaceholderViewHolder(
                ItemPlaceholderBinding.inflate(inflater, parent, false),
                mode = PlaceholderViewHolder.Mode.NOTIFICATION
            )
            VIEW_TYPE_STATUS -> StatusViewHolder(
                inflater.inflate(R.layout.item_status, parent, false),
                statusListener,
                accountId
            )
            VIEW_TYPE_STATUS_FILTERED -> FilteredStatusViewHolder(
                ItemStatusFilteredBinding.inflate(inflater, parent, false),
                statusListener
            )
            VIEW_TYPE_STATUS_NOTIFICATION -> StatusNotificationViewHolder(
                ItemStatusNotificationBinding.inflate(inflater, parent, false),
                statusListener,
                absoluteTimeFormatter
            )
            VIEW_TYPE_FOLLOW -> FollowViewHolder(
                ItemFollowBinding.inflate(inflater, parent, false),
                accountActionListener,
                statusListener
            )
            VIEW_TYPE_FOLLOW_REQUEST -> FollowRequestViewHolder(
                ItemFollowRequestBinding.inflate(inflater, parent, false),
                accountActionListener,
                statusListener,
                true
            )
            VIEW_TYPE_LOAD_MORE -> LoadMoreViewHolder(
                ItemLoadMoreBinding.inflate(inflater, parent, false),
                statusListener
            )
            VIEW_TYPE_REPORT -> ReportNotificationViewHolder(
                ItemReportNotificationBinding.inflate(inflater, parent, false),
                notificationActionListener,
                accountActionListener
            )
            VIEW_TYPE_SEVERED_RELATIONSHIP -> SeveredRelationshipNotificationViewHolder(
                ItemSeveredRelationshipNotificationBinding.inflate(inflater, parent, false),
                instanceName
            )
            VIEW_TYPE_MODERATION_WARNING -> ModerationWarningViewHolder(
                ItemModerationWarningNotificationBinding.inflate(inflater, parent, false),
                instanceName
            )
            else -> UnknownNotificationViewHolder(
                ItemUnknownNotificationBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(viewHolder, position, emptyList())
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        getItem(position)?.let { notification ->
            when (notification) {
                is NotificationViewData.Concrete ->
                    (viewHolder as NotificationsViewHolder).bind(notification, payloads, statusDisplayOptions)
                is NotificationViewData.LoadMore -> {
                    (viewHolder as LoadMoreViewHolder).setup(notification.isLoading)
                }
            }
        }
    }

    companion object {
        private const val VIEW_TYPE_PLACEHOLDER = 0
        private const val VIEW_TYPE_STATUS = 1
        private const val VIEW_TYPE_STATUS_FILTERED = 2
        private const val VIEW_TYPE_STATUS_NOTIFICATION = 3
        private const val VIEW_TYPE_FOLLOW = 4
        private const val VIEW_TYPE_FOLLOW_REQUEST = 5
        private const val VIEW_TYPE_LOAD_MORE = 6
        private const val VIEW_TYPE_REPORT = 7
        private const val VIEW_TYPE_SEVERED_RELATIONSHIP = 8
        private const val VIEW_TYPE_MODERATION_WARNING = 9
        private const val VIEW_TYPE_UNKNOWN = 10

        val NotificationsDifferCallback = object : DiffUtil.ItemCallback<NotificationViewData>() {
            override fun areItemsTheSame(
                oldItem: NotificationViewData,
                newItem: NotificationViewData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotificationViewData,
                newItem: NotificationViewData
            ): Boolean {
                return false // Items are different always. It allows to refresh timestamp on every view holder update
            }

            override fun getChangePayload(
                oldItem: NotificationViewData,
                newItem: NotificationViewData
            ): Any? {
                return if (oldItem == newItem) {
                    // If items are equal - update timestamp only
                    StatusBaseViewHolder.Key.KEY_CREATED
                } else {
                    // If items are different - update the whole view holder
                    null
                }
            }
        }
    }
}
