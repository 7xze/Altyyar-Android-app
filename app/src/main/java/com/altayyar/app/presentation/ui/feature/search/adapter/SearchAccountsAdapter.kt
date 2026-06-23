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

package com.altayyar.app.presentation.ui.feature.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.altayyar.app.presentation.ui.adapter.AccountViewHolder
import com.altayyar.app.databinding.ItemAccountBinding
import com.altayyar.app.entity.TimelineAccount
import com.altayyar.app.domain.repository.LinkListener

class SearchAccountsAdapter(private val linkListener: LinkListener, private val animateAvatars: Boolean, private val animateEmojis: Boolean, private val showBotOverlay: Boolean) :
    PagingDataAdapter<TimelineAccount, AccountViewHolder>(ACCOUNT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        getItem(position)?.let { item ->
            holder.apply {
                setupWithAccount(item, animateAvatars, animateEmojis, showBotOverlay)
                setupLinkListener(linkListener)
            }
        }
    }

    companion object {

        val ACCOUNT_COMPARATOR = object : DiffUtil.ItemCallback<TimelineAccount>() {
            override fun areContentsTheSame(
                oldItem: TimelineAccount,
                newItem: TimelineAccount
            ): Boolean = oldItem == newItem

            override fun areItemsTheSame(
                oldItem: TimelineAccount,
                newItem: TimelineAccount
            ): Boolean = oldItem.id == newItem.id
        }
    }
}
