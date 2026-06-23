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
package com.altayyar.app.adapter

import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.databinding.ItemLoadMoreBinding
import com.altayyar.app.interfaces.StatusActionListener
import com.altayyar.app.util.hide
import com.altayyar.app.util.visible

/**
 * Placeholder for missing parts in timelines.
 *
 * Displays a "Load more" button to load the gap, or a
 * circular progress bar if the missing page is being loaded.
 */
class LoadMoreViewHolder(
    private val binding: ItemLoadMoreBinding,
    listener: StatusActionListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.loadMoreButton.setOnClickListener {
            binding.loadMoreButton.hide()
            binding.loadMoreProgressBar.show()
            listener.onLoadMore(bindingAdapterPosition)
        }
    }

    fun setup(loading: Boolean) {
        binding.loadMoreButton.visible(!loading)
        binding.loadMoreProgressBar.visible(loading)
    }
}
