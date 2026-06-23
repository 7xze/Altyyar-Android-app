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

package com.altayyar.app

import android.app.Application
import android.content.SharedPreferences
import com.altayyar.app.di.PreferencesEntryPoint
import dagger.hilt.internal.GeneratedComponent
import de.c1710.filemojicompat_defaults.DefaultEmojiPackList
import de.c1710.filemojicompat_ui.helpers.EmojiPackHelper
import org.mockito.kotlin.mock

// override TayyarApplication for Robolectric tests, only initialize the necessary stuff
class TayyarApplication : Application(), PreferencesEntryPoint, GeneratedComponent {

    override fun onCreate() {
        super.onCreate()
        EmojiPackHelper.init(this, DefaultEmojiPackList.get(this))
    }

    override fun preferences(): SharedPreferences = mock {}
}
