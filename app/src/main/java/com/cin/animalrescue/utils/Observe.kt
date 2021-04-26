package com.cin.animalrescue.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer {
        it?.let { t ->
            action(t)
        }
    })
}

fun <T> LifecycleOwner.observeOnce(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer {
        it?.let { t ->
            action(t)
            liveData.removeObserver(action)
        }
    })
}
