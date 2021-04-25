package com.cin.animalrescue.data.auth

import android.content.Context
import android.content.Intent
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.SignInClient
import com.cin.animalrescue.data.sign_in_client.GoogleSignInClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthApi() : AuthApi {
    private val signInClient: SignInClient = GoogleSignInClient()

    override fun getSignInIntent(ctx: Context): Intent {
        return signInClient.getSignInIntent(ctx)
    }

    override fun signIn(intent: Intent?) {
        val credential = signInClient.getFirebaseAuthCredentialFromIntent(intent)
        Firebase.auth.signInWithCredential(credential)

        // TODO check if this code is necessary
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Sign In Complete!", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
//                }
//            }
    }

    override fun signOut(ctx: Context) {
        Firebase.auth.signOut()
        signInClient.signOut(ctx)
    }

    override fun isSignedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun getUserUID(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override fun getUserName(): String? {
        return Firebase.auth.currentUser?.displayName
    }
}
