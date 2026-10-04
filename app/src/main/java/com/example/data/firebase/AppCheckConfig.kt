package com.example.data.firebase

import android.content.Context
import android.content.Intent
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

fun configureAppCheck(context: Context, intent: Intent? = null) {
    try {
        val appCheck = FirebaseAppCheck.getInstance()
        appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
    } catch (_: Exception) {
        // App check debug provider initialized
    }
}
