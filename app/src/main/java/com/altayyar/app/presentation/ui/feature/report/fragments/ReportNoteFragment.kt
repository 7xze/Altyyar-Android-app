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

package com.altayyar.app.presentation.ui.feature.report.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.altayyar.app.R
import com.altayyar.app.presentation.ui.feature.report.ReportViewModel
import com.altayyar.app.presentation.ui.feature.report.Screen
import com.altayyar.app.databinding.FragmentReportNoteBinding
import com.altayyar.app.util.Error
import com.altayyar.app.util.Loading
import com.altayyar.app.util.Success
import com.altayyar.app.util.hide
import com.altayyar.app.util.show
import com.altayyar.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportNoteFragment : Fragment(R.layout.fragment_report_note) {

    private val viewModel: ReportViewModel by activityViewModels()

    private val binding by viewBinding(FragmentReportNoteBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fillViews()
        handleChanges()
        handleClicks()
        subscribeObservables()
    }

    private fun handleChanges() {
        binding.editNote.doAfterTextChanged {
            viewModel.reportNote = it?.toString().orEmpty()
        }
        binding.checkIsNotifyRemote.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isRemoteNotify = isChecked
        }
    }

    private fun fillViews() {
        binding.editNote.setText(viewModel.reportNote)

        if (viewModel.isRemoteAccount) {
            binding.checkIsNotifyRemote.show()
            binding.reportDescriptionRemoteInstance.show()
        } else {
            binding.checkIsNotifyRemote.hide()
            binding.reportDescriptionRemoteInstance.hide()
        }

        if (viewModel.isRemoteAccount) {
            binding.checkIsNotifyRemote.text = getString(R.string.report_remote_instance, viewModel.remoteServer)
        }
        binding.checkIsNotifyRemote.isChecked = viewModel.isRemoteNotify
    }

    private fun subscribeObservables() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reportingState.collect {
                if (it == null) return@collect
                when (it) {
                    is Success -> viewModel.navigateTo(Screen.Done)
                    is Loading -> showLoading()
                    is Error -> showError(it.cause)
                }
            }
        }
    }

    private fun showError(error: Throwable?) {
        binding.editNote.isEnabled = true
        binding.checkIsNotifyRemote.isEnabled = true
        binding.buttonReport.isEnabled = true
        binding.buttonBack.isEnabled = true
        binding.progressBar.hide()

        Snackbar.make(
            binding.buttonBack,
            if (error is IOException) R.string.error_network else R.string.error_generic,
            Snackbar.LENGTH_LONG
        )
            .setAction(R.string.action_retry) {
                sendReport()
            }
            .show()
    }

    private fun sendReport() {
        viewModel.doReport()
    }

    private fun showLoading() {
        binding.buttonReport.isEnabled = false
        binding.buttonBack.isEnabled = false
        binding.editNote.isEnabled = false
        binding.checkIsNotifyRemote.isEnabled = false
        binding.progressBar.show()
    }

    private fun handleClicks() {
        binding.buttonBack.setOnClickListener {
            viewModel.navigateTo(Screen.Back)
        }

        binding.buttonReport.setOnClickListener {
            sendReport()
        }
    }

    companion object {
        fun newInstance() = ReportNoteFragment()
    }
}
