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

package com.altayyar.app.components.notifications

import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.databinding.ItemFollowBinding
import com.altayyar.app.entity.Notification
import com.altayyar.app.interfaces.AccountActionListener
import com.altayyar.app.interfaces.LinkListener
import com.altayyar.app.util.StatusDisplayOptions
import com.altayyar.app.util.emojify
import com.altayyar.app.util.hide
import com.altayyar.app.util.loadAvatar
import com.altayyar.app.util.parseAsMastodonHtml
import com.altayyar.app.util.setClickableText
import com.altayyar.app.util.show
import com.altayyar.app.util.unicodeWrap
import com.altayyar.app.util.visible
import com.altayyar.app.viewdata.NotificationViewData

class FollowViewHolder(
    private val binding: ItemFollowBinding,
    private val listener: AccountActionListener,
    private val linkListener: LinkListener
) : RecyclerView.ViewHolder(binding.root), NotificationsViewHolder {

    override fun bind(
        viewData: NotificationViewData.Concrete,
        payloads: List<*>,
        statusDisplayOptions: StatusDisplayOptions
    ) {
        if (payloads.isNotEmpty()) {
            return
        }
        val context = itemView.context
        val account = viewData.account
        val messageTemplate =
            context.getString(if (viewData.type == Notification.Type.SignUp) R.string.notification_sign_up_format else R.string.notification_follow_format)
        val wrappedDisplayName = account.name.unicodeWrap()

        binding.notificationText.text = messageTemplate.format(wrappedDisplayName)
            .emojify(account.emojis, binding.notificationText, statusDisplayOptions.animateEmojis)

        binding.notificationUsername.text = context.getString(R.string.post_username_format, viewData.account.username)

        val emojifiedDisplayName = wrappedDisplayName.emojify(
            account.emojis,
            binding.notificationDisplayName,
            statusDisplayOptions.animateEmojis
        )
        binding.notificationDisplayName.text = emojifiedDisplayName

        if (account.note.isEmpty()) {
            binding.accountNote.hide()
        } else {
            binding.accountNote.show()

            val emojifiedNote = account.note.parseAsMastodonHtml()
                .emojify(account.emojis, binding.accountNote, statusDisplayOptions.animateEmojis)
            setClickableText(binding.accountNote, emojifiedNote, emptyList(), null, linkListener)
        }

        val avatarRadius = context.resources
            .getDimensionPixelSize(R.dimen.avatar_radius_42dp)
        loadAvatar(
            account.avatar,
            binding.notificationAvatar,
            avatarRadius,
            statusDisplayOptions.animateAvatars
        )

        binding.avatarBadge.visible(statusDisplayOptions.showBotOverlay && account.bot)

        itemView.setOnClickListener { listener.onViewAccount(account.id) }
        binding.accountNote.setOnClickListener { listener.onViewAccount(account.id) }
    }
}
