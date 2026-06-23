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
package com.altayyar.app.presentation.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.presentation.ui.feature.notifications.NotificationsViewHolder
import com.altayyar.app.databinding.ItemStatusFilteredBinding
import com.altayyar.app.entity.Filter
import com.altayyar.app.entity.FilterResult
import com.altayyar.app.domain.repository.StatusActionListener
import com.altayyar.app.util.StatusDisplayOptions
import com.altayyar.app.presentation.state.NotificationViewData
import com.altayyar.app.presentation.state.StatusViewData

class FilteredStatusViewHolder(
    private val binding: ItemStatusFilteredBinding,
    listener: StatusActionListener
) : NotificationsViewHolder, RecyclerView.ViewHolder(binding.root) {

    init {
        binding.statusFilterShowAnyway.setOnClickListener {
            listener.clearWarningAction(bindingAdapterPosition)
        }
    }

    override fun bind(
        viewData: NotificationViewData.Concrete,
        payloads: List<*>,
        statusDisplayOptions: StatusDisplayOptions
    ) {
        if (payloads.isEmpty()) {
            bind(viewData.statusViewData!!)
        }
    }

    fun bind(viewData: StatusViewData.Concrete) {
        val matchedFilterResult: FilterResult? = viewData.actionable.filtered.orEmpty().find { filterResult ->
            filterResult.filter.action == Filter.Action.WARN
        }

        val matchedFilterTitle = matchedFilterResult?.filter?.title.orEmpty()

        binding.statusFilterLabel.text = itemView.context.getString(
            R.string.status_filter_placeholder_label_format,
            matchedFilterTitle
        )
    }
}
