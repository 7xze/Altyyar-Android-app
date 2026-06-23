/* Copyright 2022 Tayyar Contributors
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

import android.content.Context
import com.altayyar.app.R
import java.util.Locale

// When a language code has changed, `language` *explicitly* returns the obsolete version,
// but `toLanguageTag()` uses the current version
// https://developer.android.com/reference/java/util/Locale#getLanguage()
val Locale.modernLanguageCode: String
    get() {
        return this.toLanguageTag().split('-', limit = 2)[0]
    }

fun Locale.getTayyarDisplayName(context: Context): String {
    return context.getString(
        R.string.language_display_name_format,
        displayLanguage,
        getDisplayLanguage(this)
    )
}
