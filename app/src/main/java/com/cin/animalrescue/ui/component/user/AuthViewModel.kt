package com.cin.animalrescue.ui.component.user

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.FirebaseAuthApi

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private var authApi: AuthApi = FirebaseAuthApi()

    fun getUserName(): String? = authApi.getUserName()
    fun getUserEmail(): String? = authApi.getUserEmail()
    fun signOut(activity: Activity) = authApi.signOut(activity)
}
