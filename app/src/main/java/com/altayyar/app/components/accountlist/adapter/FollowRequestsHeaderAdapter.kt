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

package com.altayyar.app.components.accountlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.databinding.ItemFollowRequestsHeaderBinding
import com.altayyar.app.util.BindingHolder

class FollowRequestsHeaderAdapter(
    private val instanceName: String,
    private val accountLocked: Boolean
) : RecyclerView.Adapter<BindingHolder<ItemFollowRequestsHeaderBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ItemFollowRequestsHeaderBinding> {
        val binding = ItemFollowRequestsHeaderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(
        viewHolder: BindingHolder<ItemFollowRequestsHeaderBinding>,
        position: Int
    ) {
        viewHolder.binding.root.text = viewHolder.binding.root.context.getString(R.string.follow_requests_info, instanceName)
    }

    override fun getItemCount() = if (accountLocked) 0 else 1
}
