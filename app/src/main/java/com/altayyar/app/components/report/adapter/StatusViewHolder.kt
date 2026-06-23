/* Copyright 2019 Joel Pyska
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

package com.altayyar.app.components.report.adapter

import android.text.Spanned
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.altayyar.app.R
import com.altayyar.app.components.report.model.StatusViewState
import com.altayyar.app.databinding.ItemReportStatusBinding
import com.altayyar.app.entity.Emoji
import com.altayyar.app.entity.HashTag
import com.altayyar.app.entity.Status
import com.altayyar.app.interfaces.LinkListener
import com.altayyar.app.util.AbsoluteTimeFormatter
import com.altayyar.app.util.StatusDisplayOptions
import com.altayyar.app.util.StatusViewHelper
import com.altayyar.app.util.StatusViewHelper.Companion.COLLAPSE_INPUT_FILTER
import com.altayyar.app.util.StatusViewHelper.Companion.NO_INPUT_FILTER
import com.altayyar.app.util.emojify
import com.altayyar.app.util.getRelativeTimeSpanString
import com.altayyar.app.util.hide
import com.altayyar.app.util.setClickableMentions
import com.altayyar.app.util.setClickableText
import com.altayyar.app.util.shouldTrimStatus
import com.altayyar.app.util.show
import com.altayyar.app.viewdata.StatusViewData
import com.altayyar.app.viewdata.toViewData
import java.util.Date

class StatusViewHolder(
    private val binding: ItemReportStatusBinding,
    private val statusDisplayOptions: StatusDisplayOptions,
    private val viewState: StatusViewState,
    private val adapterHandler: AdapterHandler,
    private val getStatusForPosition: (Int) -> StatusViewData.Concrete?
) : RecyclerView.ViewHolder(binding.root) {

    private val mediaViewHeight = itemView.context.resources.getDimensionPixelSize(
        R.dimen.status_media_preview_height
    )
    private val statusViewHelper = StatusViewHelper(itemView)
    private val absoluteTimeFormatter = AbsoluteTimeFormatter()

    private val previewListener = object : StatusViewHelper.MediaPreviewListener {
        override fun onViewMedia(v: View?, idx: Int) {
            viewdata()?.let { viewdata ->
                adapterHandler.showMedia(v, viewdata, idx)
            }
        }

        override fun onContentHiddenChange(isShowing: Boolean) {
            viewdata()?.id?.let { id ->
                viewState.setMediaShow(id, isShowing)
            }
        }
    }

    init {
        binding.statusSelection.setOnCheckedChangeListener { _, isChecked ->
            viewdata()?.let { viewdata ->
                adapterHandler.setStatusChecked(viewdata.status, isChecked)
            }
        }
        binding.statusMediaPreviewContainer.clipToOutline = true
    }

    fun bind(viewData: StatusViewData.Concrete) {
        binding.statusSelection.isChecked = adapterHandler.isStatusChecked(viewData.id)

        updateTextView()

        val sensitive = viewData.status.sensitive

        statusViewHelper.setMediasPreview(
            statusDisplayOptions,
            viewData.status.attachments,
            sensitive,
            previewListener,
            viewState.isMediaShow(viewData.id, viewData.status.sensitive),
            mediaViewHeight
        )

        statusViewHelper.setupPollReadonly(
            viewData.status.poll.toViewData(),
            viewData.status.emojis,
            statusDisplayOptions
        )
        setCreatedAt(viewData.status.createdAt)
    }

    private fun updateTextView() {
        viewdata()?.let { viewdata ->
            setupCollapsedState(
                shouldTrimStatus(viewdata.content),
                viewState.isCollapsed(viewdata.id, true),
                viewState.isContentShow(viewdata.id, viewdata.status.sensitive),
                viewdata.status.spoilerText
            )

            if (viewdata.status.spoilerText.isBlank()) {
                setTextVisible(
                    true,
                    viewdata.content,
                    viewdata.status.mentions,
                    viewdata.status.tags,
                    viewdata.status.emojis,
                    adapterHandler
                )
                binding.statusContentWarningButton.hide()
                binding.statusContentWarningDescription.hide()
            } else {
                val emojiSpoiler = viewdata.status.spoilerText.emojify(
                    viewdata.status.emojis,
                    binding.statusContentWarningDescription,
                    statusDisplayOptions.animateEmojis
                )
                binding.statusContentWarningDescription.text = emojiSpoiler
                binding.statusContentWarningDescription.show()
                binding.statusContentWarningButton.show()
                setContentWarningButtonText(viewState.isContentShow(viewdata.id, true))
                binding.statusContentWarningButton.setOnClickListener {
                    viewdata()?.let { viewdata ->
                        val contentShown = viewState.isContentShow(viewdata.id, true)
                        binding.statusContentWarningDescription.invalidate()
                        viewState.setContentShow(viewdata.id, !contentShown)
                        setTextVisible(
                            !contentShown,
                            viewdata.content,
                            viewdata.status.mentions,
                            viewdata.status.tags,
                            viewdata.status.emojis,
                            adapterHandler
                        )
                        setContentWarningButtonText(!contentShown)
                    }
                }
                setTextVisible(
                    viewState.isContentShow(viewdata.id, true),
                    viewdata.content,
                    viewdata.status.mentions,
                    viewdata.status.tags,
                    viewdata.status.emojis,
                    adapterHandler
                )
            }
        }
    }

    private fun setContentWarningButtonText(contentShown: Boolean) {
        if (contentShown) {
            binding.statusContentWarningButton.setText(R.string.post_content_warning_show_less)
        } else {
            binding.statusContentWarningButton.setText(R.string.post_content_warning_show_more)
        }
    }

    private fun setTextVisible(
        expanded: Boolean,
        content: Spanned,
        mentions: List<Status.Mention>,
        tags: List<HashTag>?,
        emojis: List<Emoji>,
        listener: LinkListener
    ) {
        if (expanded) {
            val emojifiedText = content.emojify(
                emojis,
                binding.statusContent,
                statusDisplayOptions.animateEmojis
            )
            setClickableText(binding.statusContent, emojifiedText, mentions, tags, listener)
        } else {
            setClickableMentions(binding.statusContent, mentions, listener)
        }
        if (binding.statusContent.text.isNullOrBlank()) {
            binding.statusContent.hide()
        } else {
            binding.statusContent.show()
        }
    }

    private fun setCreatedAt(createdAt: Date?) {
        if (statusDisplayOptions.useAbsoluteTime) {
            binding.timestampInfo.text = absoluteTimeFormatter.format(createdAt)
        } else {
            binding.timestampInfo.text = if (createdAt != null) {
                val then = createdAt.time
                val now = System.currentTimeMillis()
                getRelativeTimeSpanString(binding.timestampInfo.context, then, now)
            } else {
                // unknown minutes~
                "?m"
            }
        }
    }

    private fun setupCollapsedState(
        collapsible: Boolean,
        collapsed: Boolean,
        expanded: Boolean,
        spoilerText: String
    ) {
        /* input filter for TextViews have to be set before text */
        if (collapsible && (expanded || TextUtils.isEmpty(spoilerText))) {
            binding.buttonToggleContent.setOnClickListener {
                viewdata()?.let { viewdata ->
                    viewState.setCollapsed(viewdata.id, !collapsed)
                    updateTextView()
                }
            }

            binding.buttonToggleContent.show()
            if (collapsed) {
                binding.buttonToggleContent.setText(R.string.post_content_show_more)
                binding.statusContent.filters = COLLAPSE_INPUT_FILTER
            } else {
                binding.buttonToggleContent.setText(R.string.post_content_show_less)
                binding.statusContent.filters = NO_INPUT_FILTER
            }
        } else {
            binding.buttonToggleContent.hide()
            binding.statusContent.filters = NO_INPUT_FILTER
        }
    }

    private fun viewdata() = getStatusForPosition(bindingAdapterPosition)
}
