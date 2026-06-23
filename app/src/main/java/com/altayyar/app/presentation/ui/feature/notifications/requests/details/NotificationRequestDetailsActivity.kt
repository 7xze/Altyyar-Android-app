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

package com.altayyar.app.presentation.ui.feature.notifications.requests.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.altayyar.app.presentation.ui.activity.BottomSheetActivity
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivityNotificationRequestDetailsBinding
import com.altayyar.app.entity.Emoji
import com.altayyar.app.data.local.PrefKeys
import com.altayyar.app.util.emojify
import com.altayyar.app.util.getParcelableArrayListExtraCompat
import com.altayyar.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlin.getValue
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationRequestDetailsActivity : BottomSheetActivity() {

    private val viewModel: NotificationRequestDetailsViewModel by viewModels(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<NotificationRequestDetailsViewModel.Factory> { factory ->
                factory.create(
                    notificationRequestId = intent.getStringExtra(EXTRA_NOTIFICATION_REQUEST_ID)!!,
                    accountId = intent.getStringExtra(EXTRA_ACCOUNT_ID)!!
                )
            }
        }
    )

    private val binding by viewBinding(ActivityNotificationRequestDetailsBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)

        val animateEmojis = preferences.getBoolean(PrefKeys.ANIMATE_CUSTOM_EMOJIS, false)

        val emojis: List<Emoji> = intent.getParcelableArrayListExtraCompat(EXTRA_ACCOUNT_EMOJIS)!!

        val title = getString(R.string.notifications_from, intent.getStringExtra(EXTRA_ACCOUNT_NAME))
            .emojify(emojis, binding.includedToolbar.toolbar, animateEmojis)

        supportActionBar?.run {
            setTitle(title)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomBar) { view, insets ->
            val bottomInsets = insets.getInsets(systemBars()).bottom
            view.updatePadding(bottom = bottomInsets)
            insets.inset(0, 0, 0, bottomInsets)
        }

        lifecycleScope.launch {
            viewModel.finish.collect { finishMode ->
                setResult(RESULT_OK, Intent().apply { putExtra(EXTRA_NOTIFICATION_REQUEST_ID, intent.getStringExtra(EXTRA_NOTIFICATION_REQUEST_ID)!!) })
                finish()
            }
        }

        binding.acceptButton.setOnClickListener {
            viewModel.acceptNotificationRequest()
        }
        binding.dismissButton.setOnClickListener {
            viewModel.dismissNotificationRequest()
        }
    }

    companion object {
        const val EXTRA_NOTIFICATION_REQUEST_ID = "notificationRequestId"
        private const val EXTRA_ACCOUNT_ID = "accountId"
        private const val EXTRA_ACCOUNT_NAME = "accountName"
        private const val EXTRA_ACCOUNT_EMOJIS = "accountEmojis"
        fun newIntent(
            notificationRequestId: String,
            accountId: String,
            accountName: String,
            accountEmojis: List<Emoji>,
            context: Context
        ) = Intent(context, NotificationRequestDetailsActivity::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_REQUEST_ID, notificationRequestId)
            putExtra(EXTRA_ACCOUNT_ID, accountId)
            putExtra(EXTRA_ACCOUNT_NAME, accountName)
            putExtra(EXTRA_ACCOUNT_EMOJIS, ArrayList(accountEmojis))
        }
    }
}
