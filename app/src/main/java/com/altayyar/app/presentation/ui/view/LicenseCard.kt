/* Copyright 2018 Conny Duck
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

package com.altayyar.app.presentation.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.res.use
import com.google.android.material.R as materialR
import com.google.android.material.card.MaterialCardView
import com.altayyar.app.R
import com.altayyar.app.databinding.CardLicenseBinding
import com.altayyar.app.util.hide
import com.altayyar.app.util.openLink

class LicenseCard
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = materialR.attr.materialCardViewFilledStyle
) : MaterialCardView(context, attrs, defStyleAttr) {

    init {
        val binding = CardLicenseBinding.inflate(LayoutInflater.from(context), this)

        val (name, license, link) = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LicenseCard,
            0,
            0
        ).use { a ->
            Triple(
                a.getString(R.styleable.LicenseCard_name),
                a.getString(R.styleable.LicenseCard_license),
                a.getString(R.styleable.LicenseCard_link)
            )
        }

        binding.licenseCardName.text = name
        binding.licenseCardLicense.text = license
        if (link.isNullOrBlank()) {
            binding.licenseCardLink.hide()
        } else {
            binding.licenseCardLink.text = link
            setOnClickListener { context.openLink(link) }
        }
    }
}
