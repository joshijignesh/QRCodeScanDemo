package com.qrcodescannerdemo.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

class StartSettingsActivityContract : ActivityResultContract<Int, String?>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            val uri = Uri.fromParts("package", context.packageName, null)
            this.data = uri
        }
    }
    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return ""
    }
}