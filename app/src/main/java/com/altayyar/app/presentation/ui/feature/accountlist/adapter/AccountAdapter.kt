/* Copyright 2021 Tayyar Contributors.
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
package com.altayyar.app.presentation.ui.feature.accountlist.adapter

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.presentation.state.AccountViewData
import com.altayyar.app.domain.repository.AccountActionListener

abstract class AccountAdapter<AVH : RecyclerView.ViewHolder>(
    protected val accountActionListener: AccountActionListener,
    protected val animateAvatar: Boolean,
    protected val animateEmojis: Boolean,
    protected val showBotOverlay: Boolean
) : PagingDataAdapter<AccountViewData, AVH>(AccountViewDataDifferCallback) {

    companion object {
        private val AccountViewDataDifferCallback = object : DiffUtil.ItemCallback<AccountViewData>() {
            override fun areItemsTheSame(
                oldItem: AccountViewData,
                newItem: AccountViewData
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AccountViewData,
                newItem: AccountViewData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
