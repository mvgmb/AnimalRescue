package com.cin.animalrescue.data.auth

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.SignInClient
import com.cin.animalrescue.data.sign_in_client.GoogleSignInClient
import com.cin.animalrescue.vo.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseAuthApi() : AuthApi {
    private val signInClient: SignInClient = GoogleSignInClient()

    override fun getSignInIntent(ctx: Context): Intent {
        return signInClient.getSignInIntent(ctx)
    }

    override fun signIn(intent: Intent?): LiveData<Resource<Boolean>> {
        val res = MutableLiveData<Resource<Boolean>>()

        val credential = signInClient.getFirebaseAuthCredentialFromIntent(intent)
        Firebase.auth.signInWithCredential(credential)
            .addOnSuccessListener {
                res.postValue(Resource.success(true))
            }
            .addOnFailureListener { e ->
                res.postValue(Resource.error(e.toString(), null))
            }

        return res
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

    override fun getUserEmail(): String? {
        return Firebase.auth.currentUser?.email
    }
}
