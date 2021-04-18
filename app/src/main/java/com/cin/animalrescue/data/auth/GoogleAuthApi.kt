package com.cin.animalrescue.data.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.cin.animalrescue.R
import com.cin.animalrescue.data.AuthApi
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthApi(ctx: Context) : AuthApi {
    private var googleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ctx.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(ctx, gso)
    }

    override fun getSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    override fun signOut() {
        // TODO return feedback
        googleSignInClient.signOut()
    }

    override fun getFirebaseAuthCredentialFromIntent(intent: Intent?): AuthCredential {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        try {
            val account = task.getResult(ApiException::class.java)!!
            return GoogleAuthProvider.getCredential(account.idToken!!, null)
        } catch (e: ApiException) {
            // TODO improve Logging
            Log.e("MY_TAG", "Exception: $e")
            throw e
        }
    }
}