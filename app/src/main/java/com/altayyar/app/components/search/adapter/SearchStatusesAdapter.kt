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

package com.altayyar.app.components.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.altayyar.app.R
import com.altayyar.app.adapter.StatusBaseViewHolder
import com.altayyar.app.adapter.StatusViewHolder
import com.altayyar.app.interfaces.StatusActionListener
import com.altayyar.app.util.StatusDisplayOptions
import com.altayyar.app.viewdata.StatusViewData

class SearchStatusesAdapter(
    private val statusDisplayOptions: StatusDisplayOptions,
    private val statusListener: StatusActionListener
) : PagingDataAdapter<StatusViewData.Concrete, StatusViewHolder>(STATUS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        onBindViewHolder(holder, position, emptyList())
    }

    override fun onBindViewHolder(holder: StatusViewHolder, position: Int, payloads: List<Any>) {
        getItem(position)?.let { item ->
            holder.setupWithStatus(item, statusListener, statusDisplayOptions, payloads, true)
        }
    }

    companion object {

        val STATUS_COMPARATOR = object : DiffUtil.ItemCallback<StatusViewData.Concrete>() {
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
