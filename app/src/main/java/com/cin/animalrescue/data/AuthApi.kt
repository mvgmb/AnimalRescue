package com.cin.animalrescue.data

import android.content.Intent

interface AuthApi {
    fun getSignInIntent(): Intent
    fun signIn(intent: Intent?)
    fun signOut()
    fun isSignedIn(): Boolean
    fun getUserUID(): String?
    fun getUserName(): String?
}
