package com.cin.animalrescue.utils

import android.util.Log

object Logger {
    private const val TAG = "MyLogger"

    fun logInfo(msg: String) {
        Log.i(TAG, msg)
    }

    fun logError(msg: String) {
        Log.e(TAG, msg)
    }

    fun logDebug(msg: String) {
        Log.d(TAG, msg)
    }
}
