package com.cin.animalrescue.utils

import android.util.Log

object Logger {
    private const val TAG = "MyLogger"

    fun info(msg: String) {
        Log.i(TAG, msg)
    }

    fun error(msg: String) {
        Log.e(TAG, msg)
    }

    fun debug(msg: String) {
        Log.d(TAG, msg)
    }
}
