/* Copyright 2020 Tayyar Contributors
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
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.core.content.edit
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.altayyar.app.settings.AppTheme
import com.altayyar.app.settings.NEW_INSTALL_SCHEMA_VERSION
import com.altayyar.app.settings.PrefKeys
import com.altayyar.app.settings.PrefKeys.APP_THEME
import com.altayyar.app.settings.SCHEMA_VERSION
import com.altayyar.app.util.LocaleManager
import com.altayyar.app.util.setAppNightMode
import com.altayyar.app.worker.PruneCacheWorker
import dagger.hilt.android.HiltAndroidApp
import de.c1710.filemojicompat_defaults.DefaultEmojiPackList
import de.c1710.filemojicompat_ui.helpers.EmojiPackHelper
import de.c1710.filemojicompat_ui.helpers.EmojiPreference
import java.security.Security
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import org.conscrypt.Conscrypt

@HiltAndroidApp
class TayyarApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var localeManager: LocaleManager

    @Inject
    lateinit var preferences: SharedPreferences

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        // Uncomment me to get StrictMode violation logs
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()
//                    .detectUnbufferedIo()
//                    .penaltyLog()
//                    .build())
//        }
        super.onCreate()

        try {
            Security.insertProviderAt(Conscrypt.newProvider(), 1)
        } catch (e: Throwable) {
            Log.e(TAG, "Failed to insert Conscrypt provider", e)
        }

        val workManager = WorkManager.getInstance(this)

        // Migrate shared preference keys and defaults from version to version.
        val oldVersion = preferences.getInt(
            PrefKeys.SCHEMA_VERSION,
            NEW_INSTALL_SCHEMA_VERSION
        )
        if (oldVersion != SCHEMA_VERSION) {
            if (oldVersion < 2025021701) {
                // A new periodic work request is enqueued by unique name (and not tag anymore): stop the old one
                workManager.cancelAllWorkByTag("pullNotifications")
            }
            if (oldVersion < 2025032401 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // delete old now unused notification channels
                for (channel in notificationManager.notificationChannels) {
                    if (channel.id.startsWith("CHANNEL_SIGN_UP") || channel.id.startsWith("CHANNEL_REPORT") || channel.id.startsWith("CHANNEL_BOOST")) {
                        notificationManager.deleteNotificationChannel(channel.id)
                    }
                }
            }

            upgradeSharedPreferences(oldVersion, SCHEMA_VERSION)
        }

        // In this case, we want to have the emoji preferences merged with the other ones
        // Copied from PreferenceManager.getDefaultSharedPreferenceName
        EmojiPreference.sharedPreferenceName = packageName + "_preferences"
        EmojiPackHelper.init(this, DefaultEmojiPackList.get(this), allowPackImports = false)

        // init night mode
        val theme = preferences.getString(APP_THEME, AppTheme.DEFAULT.value)
        setAppNightMode(theme)

        localeManager.setLocale()

        // Prune the database every ~ 12 hours when the device is idle.
        val pruneCacheWorker = PeriodicWorkRequestBuilder<PruneCacheWorker>(12, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiresDeviceIdle(true).build())
            .build()
        workManager.enqueueUniquePeriodicWork(
            PruneCacheWorker.PERIODIC_WORK_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            pruneCacheWorker
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun upgradeSharedPreferences(oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Upgrading shared preferences: $oldVersion -> $newVersion")
        preferences.edit {
            if (oldVersion < 2023022701) {
                // These preferences are (now) handled in AccountPreferenceHandler. Remove them from shared for clarity.

                remove(PrefKeys.ALWAYS_OPEN_SPOILER)
                remove(PrefKeys.ALWAYS_SHOW_SENSITIVE_MEDIA)
                remove(PrefKeys.MEDIA_PREVIEW_ENABLED)
            }

            if (oldVersion != NEW_INSTALL_SCHEMA_VERSION && oldVersion < 2023082301) {
                // Default value for appTheme is now THEME_SYSTEM. If the user is upgrading and
                // didn't have an explicit preference set use the previous default, so the
                // theme does not unexpectedly change.
                if (!preferences.contains(APP_THEME)) {
                    putString(APP_THEME, AppTheme.NIGHT.value)
                }
            }

            if (oldVersion < 2023112001) {
                remove(PrefKeys.TAB_FILTER_HOME_REPLIES)
                remove(PrefKeys.TAB_FILTER_HOME_BOOSTS)
                remove(PrefKeys.TAB_SHOW_HOME_SELF_BOOSTS)
            }

            if (oldVersion < 2024060201) {
                remove(PrefKeys.Deprecated.FAB_HIDE)
            }

            putInt(PrefKeys.SCHEMA_VERSION, newVersion)
        }
    }

    companion object {
        private const val TAG = "TayyarApplication"
    }
}
