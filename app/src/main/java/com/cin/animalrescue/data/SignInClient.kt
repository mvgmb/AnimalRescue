package com.cin.animalrescue.data

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.AuthCredential

interface SignInClient {
    fun getSignInIntent(ctx: Context): Intent
    fun signOut(ctx: Context)
    fun getFirebaseAuthCredentialFromIntent(intent: Intent?): AuthCredential
}
