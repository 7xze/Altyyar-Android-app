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
import com.altayyar.app.databinding.ItemHashtagBinding
import com.altayyar.app.entity.HashTag
import com.altayyar.app.interfaces.LinkListener
import com.altayyar.app.util.BindingHolder

class SearchHashtagsAdapter(private val linkListener: LinkListener) :
    PagingDataAdapter<HashTag, BindingHolder<ItemHashtagBinding>>(HASHTAG_COMPARATOR) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ItemHashtagBinding> {
        val binding = ItemHashtagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemHashtagBinding>, position: Int) {
        getItem(position)?.let { (name) ->
            holder.binding.root.text = holder.binding.root.context.getString(R.string.hashtag_format, name)
            holder.binding.root.setOnClickListener { linkListener.onViewTag(name) }
        }
    }

    companion object {

        val HASHTAG_COMPARATOR = object : DiffUtil.ItemCallback<HashTag>() {
            override fun areContentsTheSame(oldItem: HashTag, newItem: HashTag): Boolean =
                oldItem.name == newItem.name

            override fun areItemsTheSame(oldItem: HashTag, newItem: HashTag): Boolean =
                oldItem.name == newItem.name
        }
    }
}
