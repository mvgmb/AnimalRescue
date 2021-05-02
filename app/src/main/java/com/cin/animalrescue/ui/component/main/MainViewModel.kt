package com.cin.animalrescue.ui.component.main

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.FirebaseAuthApi
import com.cin.animalrescue.vo.Resource

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val authApi: AuthApi = FirebaseAuthApi()

    fun getSignInIntent(ctx: Context): Intent = authApi.getSignInIntent(ctx)
    fun signIn(intent: Intent?): LiveData<Resource<Boolean>> = authApi.signIn(intent)
    fun isSignedIn(): Boolean = authApi.isSignedIn()
}
