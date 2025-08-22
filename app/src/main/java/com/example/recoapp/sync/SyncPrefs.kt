package com.example.recoapp.sync

import android.content.Context
import android.content.SharedPreferences

class SyncPrefs(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getLastSync(): Long = prefs.getLong(KEY_LAST_SYNC, 0L)

    fun setLastSync(ts: Long) {
        prefs.edit().putLong(KEY_LAST_SYNC, ts).apply()
    }

    companion object {
        private const val PREFS_NAME = "recoapp_sync_prefs"
        private const val KEY_LAST_SYNC = "last_sync"
    }
}
