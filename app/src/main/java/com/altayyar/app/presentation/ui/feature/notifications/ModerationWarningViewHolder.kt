/* Copyright 2025 Tayyar Contributors
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

package com.altayyar.app.presentation.ui.feature.notifications

import android.content.Intent
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.databinding.ItemModerationWarningNotificationBinding
import com.altayyar.app.entity.AccountWarning
import com.altayyar.app.util.StatusDisplayOptions
import com.altayyar.app.presentation.state.NotificationViewData

class ModerationWarningViewHolder(
    private val binding: ItemModerationWarningNotificationBinding,
    private val instanceDomain: String
) : RecyclerView.ViewHolder(binding.root), NotificationsViewHolder {

    override fun bind(
        viewData: NotificationViewData.Concrete,
        payloads: List<*>,
        statusDisplayOptions: StatusDisplayOptions
    ) {
        if (payloads.isNotEmpty()) {
            return
        }
        val warning = viewData.moderationWarning!!

        binding.moderationWarningDescription.setText(
            when (warning.action) {
                AccountWarning.Action.NONE -> R.string.moderation_warning_action_none
                AccountWarning.Action.DISABLE -> R.string.moderation_warning_action_disable
                AccountWarning.Action.MARK_STATUSES_AS_SENSITIVE -> R.string.moderation_warning_action_mark_statuses_as_sensitive
                AccountWarning.Action.DELETE_STATUSES -> R.string.moderation_warning_action_delete_statuses
                AccountWarning.Action.SENSITIVE -> R.string.moderation_warning_action_sensitive
                AccountWarning.Action.SILENCE -> R.string.moderation_warning_action_silence
                AccountWarning.Action.SUSPEND -> R.string.moderation_warning_action_suspend
            }
        )

        binding.root.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://$instanceDomain/disputes/strikes/${warning.id}".toUri())
            binding.root.context.startActivity(intent)
        }
    }
}
