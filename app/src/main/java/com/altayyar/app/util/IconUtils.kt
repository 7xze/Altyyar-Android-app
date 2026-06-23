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

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.color.MaterialColors
import com.altayyar.app.R

fun PreferenceFragmentCompat.icon(icon: Int): Drawable? {
    val context = requireContext()
    return AppCompatResources.getDrawable(context, icon)?.apply {
        setTint(MaterialColors.getColor(context, R.attr.iconColor, Color.BLACK))
    }
}
