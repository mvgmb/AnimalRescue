package com.cin.animalrescue.data

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import com.cin.animalrescue.vo.Resource

interface AuthApi {
    fun getSignInIntent(ctx: Context): Intent
    fun signIn(intent: Intent?): LiveData<Resource<Boolean>>
    fun signOut(ctx: Context)
    fun isSignedIn(): Boolean
    fun getUserUID(): String?
    fun getUserName(): String?
    fun getUserEmail(): String?
}
