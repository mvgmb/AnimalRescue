package com.cin.animalrescue.data

import android.content.Context
import android.content.Intent

interface AuthApi {
    fun getSignInIntent(ctx: Context): Intent
    fun signIn(intent: Intent?)
    fun signOut(ctx: Context)
    fun isSignedIn(): Boolean
    fun getUserUID(): String?
    fun getUserName(): String?
    fun getUserEmail(): String?
}
