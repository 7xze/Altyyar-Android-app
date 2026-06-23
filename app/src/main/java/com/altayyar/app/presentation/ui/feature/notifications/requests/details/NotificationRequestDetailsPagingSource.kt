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

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.altayyar.app.presentation.state.NotificationViewData

class NotificationRequestDetailsPagingSource(
    private val notifications: List<NotificationViewData>,
    private val nextKey: String?
) : PagingSource<String, NotificationViewData>() {
    override fun getRefreshKey(state: PagingState<String, NotificationViewData>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, NotificationViewData> {
        return if (params is LoadParams.Refresh) {
            LoadResult.Page(notifications.toList(), null, nextKey)
        } else {
            LoadResult.Page(emptyList(), null, null)
        }
    }
}
