package com.cin.animalrescue.data.sign_in_client

import android.content.Context
import android.content.Intent
import android.util.Log
import com.cin.animalrescue.R
import com.cin.animalrescue.data.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInClient() : SignInClient {

    override fun getSignInIntent(ctx: Context): Intent {
        return getClient(ctx).signInIntent
    }

    override fun signOut(ctx: Context) {
        getClient(ctx).signOut()
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

    private fun getClient(ctx: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(ctx.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(ctx, gso)
    }
}
