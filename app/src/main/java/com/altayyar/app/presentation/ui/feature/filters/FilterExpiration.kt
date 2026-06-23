/* Copyright Tayyar Contributors
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

package com.altayyar.app.presentation.ui.feature.filters

import kotlin.jvm.JvmInline

/**
 * Custom class to have typesafety for filter expirations.
 * Retrofit will call toString when sending this class as part of a form-urlencoded body.
 */
@JvmInline
value class FilterExpiration private constructor(val seconds: Int) {

    override fun toString(): String {
        return if (seconds < 0) "" else seconds.toString()
    }

    companion object {
        val unchanged: FilterExpiration? = null
        val never: FilterExpiration = FilterExpiration(-1)

        fun seconds(seconds: Int): FilterExpiration = FilterExpiration(seconds)
    }
}
