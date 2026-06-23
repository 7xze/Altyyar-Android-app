package com.altayyar.app.adapter

import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.databinding.ItemAccountBinding
import com.altayyar.app.entity.TimelineAccount
import com.altayyar.app.interfaces.AccountActionListener
import com.altayyar.app.interfaces.LinkListener
import com.altayyar.app.util.emojify
import com.altayyar.app.util.loadAvatar
import com.altayyar.app.util.visible

class AccountViewHolder(
    private val binding: ItemAccountBinding
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var accountId: String

    fun setupWithAccount(
        account: TimelineAccount,
        animateAvatar: Boolean,
        animateEmojis: Boolean,
        showBotOverlay: Boolean
    ) {
        accountId = account.id

        binding.accountUsername.text = binding.accountUsername.context.getString(
            R.string.post_username_format,
            account.username
        )

        val emojifiedName = account.name.emojify(
            account.emojis,
            binding.accountDisplayName,
            animateEmojis
        )
        binding.accountDisplayName.text = emojifiedName

        val avatarRadius = binding.accountAvatar.context.resources
            .getDimensionPixelSize(R.dimen.avatar_radius_48dp)
        loadAvatar(account.avatar, binding.accountAvatar, avatarRadius, animateAvatar)

        binding.accountBotBadge.visible(showBotOverlay && account.bot)
    }

    fun setupActionListener(listener: AccountActionListener) {
        itemView.setOnClickListener { listener.onViewAccount(accountId) }
    }

    fun setupLinkListener(listener: LinkListener) {
        itemView.setOnClickListener {
            listener.onViewAccount(
                accountId
            )
        }
    }
}
