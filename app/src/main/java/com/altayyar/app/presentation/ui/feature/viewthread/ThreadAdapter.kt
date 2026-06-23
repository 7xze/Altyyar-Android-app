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

package com.altayyar.app.presentation.ui.feature.viewthread

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.presentation.ui.adapter.FilteredStatusViewHolder
import com.altayyar.app.presentation.ui.adapter.StatusBaseViewHolder
import com.altayyar.app.presentation.ui.adapter.StatusDetailedViewHolder
import com.altayyar.app.presentation.ui.adapter.StatusViewHolder
import com.altayyar.app.databinding.ItemStatusFilteredBinding
import com.altayyar.app.entity.Filter
import com.altayyar.app.domain.repository.StatusActionListener
import com.altayyar.app.util.StatusDisplayOptions
import com.altayyar.app.presentation.state.StatusViewData

class ThreadAdapter(
    private val statusDisplayOptions: StatusDisplayOptions,
    private val statusActionListener: StatusActionListener
) : ListAdapter<StatusViewData.Concrete, RecyclerView.ViewHolder>(ThreadDifferCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_STATUS ->
                StatusViewHolder(inflater.inflate(R.layout.item_status, parent, false))
            VIEW_TYPE_STATUS_FILTERED ->
                FilteredStatusViewHolder(
                    ItemStatusFilteredBinding.inflate(inflater, parent, false),
                    statusActionListener
                )
            VIEW_TYPE_STATUS_DETAILED ->
                StatusDetailedViewHolder(
                    inflater.inflate(R.layout.item_status_detailed, parent, false)
                )
            else -> error("Unknown item type: $viewType")
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        onBindViewHolder(viewHolder, position, emptyList())
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        val status = getItem(position)
        if (viewHolder is FilteredStatusViewHolder) {
            viewHolder.bind(status)
        } else if (viewHolder is StatusBaseViewHolder) {
            viewHolder.setupWithStatus(status, statusActionListener, statusDisplayOptions, payloads, false)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val viewData = getItem(position)
        return if (viewData.isDetailed) {
            VIEW_TYPE_STATUS_DETAILED
        } else if (viewData.filter?.action == Filter.Action.WARN) {
            VIEW_TYPE_STATUS_FILTERED
        } else {
            VIEW_TYPE_STATUS
        }
    }

    companion object {
        private const val VIEW_TYPE_STATUS = 0
        private const val VIEW_TYPE_STATUS_DETAILED = 1
        private const val VIEW_TYPE_STATUS_FILTERED = 2

        val ThreadDifferCallback = object : DiffUtil.ItemCallback<StatusViewData.Concrete>() {
            override fun areItemsTheSame(
                oldItem: StatusViewData.Concrete,
                newItem: StatusViewData.Concrete
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StatusViewData.Concrete,
                newItem: StatusViewData.Concrete
            ): Boolean {
                return false // Items are different always. It allows to refresh timestamp on every view holder update
            }

            override fun getChangePayload(
                oldItem: StatusViewData.Concrete,
                newItem: StatusViewData.Concrete
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
