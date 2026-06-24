/*
 * Copyright 2023 Tayyar Contributors
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
 * see <http://www.gnu.org/licenses>.
 */

@file:JvmName("ViewDataUtils")

/* Copyright 2017 Andrew Dawson
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

package com.altayyar.app.util

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.altayyar.app.entity.Filter
import com.altayyar.app.entity.Status
import com.altayyar.app.entity.TrendingTag
import com.altayyar.app.presentation.state.StatusViewData
import com.altayyar.app.presentation.state.TranslationViewData
import com.altayyar.app.presentation.state.TrendingViewData

fun Status.toViewData(
    isShowingContent: Boolean,
    isExpanded: Boolean,
    isCollapsed: Boolean,
    isDetailed: Boolean = false,
    filter: Filter?,
    translation: TranslationViewData? = null,
) = StatusViewData.Concrete(
    status = this,
    isShowingContent = isShowingContent,
    isCollapsed = isCollapsed,
    isExpanded = isExpanded,
    isDetailed = isDetailed,
    translation = translation,
).apply { this.filter = filter }

fun List<TrendingTag>.toViewData(): List<TrendingViewData.Tag> {
    val maxTrendingValue = flatMap { tag -> tag.history }
        .mapNotNull { it.uses.toLongOrNull() }
        .maxOrNull() ?: 1

    return map { tag ->

        val reversedHistory = tag.history.asReversed()

        TrendingViewData.Tag(
            name = tag.name,
            usage = reversedHistory.mapNotNull { it.uses.toLongOrNull() },
            accounts = reversedHistory.mapNotNull { it.accounts.toLongOrNull() },
            maxTrendingValue = maxTrendingValue
        )
    }
}

fun CombinedLoadStates.isAnyLoading(): Boolean {
    return this.refresh == LoadState.Loading || this.append == LoadState.Loading || this.prepend == LoadState.Loading
}
