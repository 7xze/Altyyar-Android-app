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

package com.altayyar.app.presentation.ui.feature.timeline.viewmodel

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.altayyar.app.presentation.ui.feature.timeline.util.ifExpected
import com.altayyar.app.util.HttpHeaderLink
import com.altayyar.app.util.toViewData
import com.altayyar.app.presentation.state.StatusViewData
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class NetworkTimelineRemoteMediator(
    private val viewModel: NetworkTimelineViewModel
) : RemoteMediator<String, StatusViewData>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, StatusViewData>
    ): MediatorResult {
        try {
            val statusResponse = when (loadType) {
                LoadType.REFRESH -> {
                    viewModel.fetchStatusesForKind(null, null, limit = state.config.pageSize)
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val maxId = viewModel.nextKey
                    if (maxId != null) {
                        viewModel.fetchStatusesForKind(maxId, null, limit = state.config.pageSize)
                    } else {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                }
            }

            val statuses = statusResponse.body()
            if (!statusResponse.isSuccessful || statuses == null) {
                return MediatorResult.Error(HttpException(statusResponse))
            }

            val activeAccount = viewModel.activeAccountFlow.value!!

            val data = statuses.map { status ->

                val oldStatus = viewModel.statusData.find { s ->
                    s.asStatusOrNull()?.id == status.id
                }?.asStatusOrNull()

                val filter = oldStatus?.filter ?: status.getApplicableFilter(viewModel.kind.toFilterKind())
                val contentShowing = oldStatus?.isShowingContent ?: status.shouldShowContent(activeAccount.alwaysShowSensitiveMedia, viewModel.kind.toFilterKind())
                val expanded = oldStatus?.isExpanded ?: activeAccount.alwaysOpenSpoiler
                val contentCollapsed = oldStatus?.isCollapsed != false

                status.toViewData(
                    isShowingContent = contentShowing,
                    isExpanded = expanded,
                    isCollapsed = contentCollapsed,
                    filter = filter,
                )
            }

            if (loadType == LoadType.REFRESH && viewModel.statusData.isNotEmpty()) {
                val insertPlaceholder = if (statuses.isNotEmpty()) {
                    !viewModel.statusData.removeAll { statusViewData ->
                        statuses.any { status -> status.id == statusViewData.asStatusOrNull()?.id }
                    }
                } else {
                    false
                }

                viewModel.statusData.addAll(0, data)

                if (insertPlaceholder) {
                    viewModel.statusData[statuses.size - 1] = StatusViewData.LoadMore(statuses.last().id, false)
                }
            } else {
                val linkHeader = statusResponse.headers()["Link"]
                val links = HttpHeaderLink.parse(linkHeader)
                val next = HttpHeaderLink.findByRelationType(links, "next")
                viewModel.nextKey = next?.uri?.getQueryParameter("max_id")

                viewModel.statusData.addAll(data)
            }

            viewModel.currentSource?.invalidate()
            return MediatorResult.Success(endOfPaginationReached = statuses.isEmpty())
        } catch (e: Exception) {
            return ifExpected(e) {
                Log.w(TAG, "Failed to load timeline", e)
                MediatorResult.Error(e)
            }
        }
    }

    companion object {
        private const val TAG = "NetworkTimelineRM"
    }
}
