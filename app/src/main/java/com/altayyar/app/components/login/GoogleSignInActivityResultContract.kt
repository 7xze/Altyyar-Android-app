package com.altayyar.app.components.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class GoogleSignInActivityResultContract : ActivityResultContract<Intent, Intent?>() {
    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
        if (resultCode != Activity.RESULT_OK) return null
        return intent
    }
}
