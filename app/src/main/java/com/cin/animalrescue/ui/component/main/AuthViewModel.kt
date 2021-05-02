package com.cin.animalrescue.ui.component.main

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.FirebaseAuthApi
import com.cin.animalrescue.vo.Resource

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private var authApi: AuthApi = FirebaseAuthApi()

    fun signIn(intent: Intent?): LiveData<Resource<Boolean>> {
        return authApi.signIn(intent)
    }

    fun isSignedIn(): Boolean {
        return authApi.isSignedIn()
    }

    fun getSignInIntent(ctx: Context): Intent{
        return authApi.getSignInIntent(ctx)
    }
}
