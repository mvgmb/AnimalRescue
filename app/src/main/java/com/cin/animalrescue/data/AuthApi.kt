package com.cin.animalrescue.data

import android.content.Intent
import com.google.firebase.auth.AuthCredential

interface AuthApi {
    fun getSignInIntent(): Intent
    fun signOut()
    fun getFirebaseAuthCredentialFromIntent(intent: Intent?): AuthCredential
}