/* Copyright 2019 Levi Bard
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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.altayyar.app.R
import com.altayyar.app.databinding.ItemAutocompleteAccountBinding
import com.altayyar.app.db.entity.AccountEntity
import com.altayyar.app.util.emojify
import com.altayyar.app.util.loadAvatar

class AccountSelectionAdapter(
    context: Context,
    private val animateAvatars: Boolean,
    private val animateEmojis: Boolean
) : ArrayAdapter<AccountEntity>(
    context,
    R.layout.item_autocomplete_account
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            ItemAutocompleteAccountBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemAutocompleteAccountBinding.bind(convertView)
        }

        val account = getItem(position)
        if (account != null) {
            binding.username.text = account.fullName
            binding.displayName.text = account.displayName.emojify(account.emojis, binding.displayName, animateEmojis)
            binding.avatarBadge.visibility = View.GONE // We never want to display the bot badge here

            val avatarRadius = context.resources.getDimensionPixelSize(R.dimen.avatar_radius_42dp)

            loadAvatar(account.profilePictureUrl, binding.avatar, avatarRadius, animateAvatars)
        }

        return binding.root
    }
}
