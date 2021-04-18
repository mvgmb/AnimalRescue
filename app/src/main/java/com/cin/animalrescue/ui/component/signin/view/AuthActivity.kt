package com.cin.animalrescue.ui.component.signin.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.cin.animalrescue.R
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.GoogleAuthApi
import com.cin.animalrescue.databinding.ActivityAuthBinding
import com.cin.animalrescue.ui.component.main.view.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.task.ui.base.BaseActivity

class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authApi: AuthApi

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }


    override fun initViewBinding() {
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.user.text = "Please, SignIn."

        authApi = GoogleAuthApi(this)
        auth = Firebase.auth
        binding.user.text = auth.currentUser?.displayName

        binding.btnSignIn.setOnClickListener {
            signIn()
            binding.user.text = auth.currentUser?.displayName
        }

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            authApi.signOut()
            binding.user.text = auth.currentUser?.displayName
        }
    }

    override fun observeViewModel() {}

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        Toast.makeText(this, "User: ${currentUser?.uid}", Toast.LENGTH_SHORT).show()
    }

    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = authApi.getFirebaseAuthCredentialFromIntent(result.data)
                    firebaseAuthWithGoogle(credential)
                } catch (e: ApiException) {
                    Toast.makeText(this, "Sign In Failed: $e", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun signIn() {
        val intent = authApi.getSignInIntent()
        register.launch(intent)
    }

    // TODO refactor firebase auth to separate class
    private fun firebaseAuthWithGoogle(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sign In Complete!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}