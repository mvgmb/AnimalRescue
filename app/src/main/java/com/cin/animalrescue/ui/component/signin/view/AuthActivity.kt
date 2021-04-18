package com.cin.animalrescue.ui.component.signin.view

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.cin.animalrescue.data.SignInClient
import com.cin.animalrescue.databinding.ActivityAuthBinding
import com.cin.animalrescue.ui.component.main.view.MainActivity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.task.ui.base.BaseActivity

class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding

    private lateinit var signInClient: SignInClient
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    override fun initViewBinding() {
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signInClient = com.cin.animalrescue.data.auth.GoogleSignInClient(this)
        auth = Firebase.auth

        binding.user.text = auth.currentUser?.displayName

        binding.btnSignIn.setOnClickListener {
            signIn()
            binding.user.text = auth.currentUser?.displayName
        }

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            signInClient.signOut()
            binding.user.text = auth.currentUser?.displayName
        }
    }

    override fun observeViewModel() {}

    private val register = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        function(it)
    }

    private fun signIn() {
        val intent = signInClient.getSignInIntent()
        register.launch(intent)
    }

    // TODO vvvvvvvvvvvvvvvvvvv refactor firebase auth to separate class vvvvvvvvvvvvvvvvvvvvv

    private fun function(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = signInClient.getFirebaseAuthCredentialFromIntent(result.data)
                firebaseAuthWithGoogle(credential)
            } catch (e: ApiException) {
                Toast.makeText(this, "Sign In Failed: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }

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