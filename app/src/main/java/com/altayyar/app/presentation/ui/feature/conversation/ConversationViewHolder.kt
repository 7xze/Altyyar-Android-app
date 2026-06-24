package com.altayyar.app.presentation.ui.feature.conversation

import android.view.View
import android.widget.TextView
import com.altayyar.app.R
import com.altayyar.app.presentation.state.ConversationViewData
import com.altayyar.app.presentation.ui.adapter.StatusBaseViewHolder
import com.altayyar.app.domain.repository.StatusActionListener
import com.altayyar.app.util.StatusDisplayOptions

class ConversationViewHolder(
    itemView: View,
    private val statusDisplayOptions: StatusDisplayOptions,
    private val listener: StatusActionListener
) : StatusBaseViewHolder(itemView) {

    private val conversationName: TextView = itemView.findViewById(R.id.conversation_name)
    private val avatar1: View = itemView.findViewById(R.id.status_avatar_1)
    private val avatar2: View = itemView.findViewById(R.id.status_avatar_2)

    fun setupWithConversation(conversation: ConversationViewData, payloads: List<Any>) {
        setupWithStatus(conversation.lastStatus, listener, statusDisplayOptions, payloads, true)

        val context = conversationName.context
        val names = conversation.accounts.map { it.displayName }
        val nameText = when (names.size) {
            0 -> ""
            1 -> context.getString(R.string.conversation_1_recipients, names[0])
            2 -> context.getString(R.string.conversation_2_recipients, names[0], names[1])
            else -> context.getString(R.string.conversation_more_recipients, names[0], names[1], names.size - 2)
        }
        conversationName.text = nameText
        conversationName.visibility = View.VISIBLE

        if (conversation.accounts.size >= 2) {
            avatar1.visibility = View.VISIBLE
            avatar2.visibility = View.VISIBLE
        } else {
            avatar1.visibility = View.GONE
            avatar2.visibility = View.GONE
        }
    }
}
