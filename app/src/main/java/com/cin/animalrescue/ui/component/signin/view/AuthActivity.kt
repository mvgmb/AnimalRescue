package com.cin.animalrescue.ui.component.signin.view

import android.app.Activity
import android.content.Intent
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.FirebaseAuthApi
import com.cin.animalrescue.databinding.ActivityAuthBinding
import com.cin.animalrescue.ui.component.main.view.MainActivity
import com.google.android.gms.common.api.ApiException
import com.task.ui.base.BaseActivity
import kotlinx.coroutines.*

class AuthActivity : BaseActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var authApi: AuthApi

    override fun initViewBinding() {
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authApi = FirebaseAuthApi()

        bindUIs()
    }

    override fun observeViewModel() {}

    private fun bindUIs() {
        binding.user.text = authApi.getUserName()

        binding.btnSignIn.setOnClickListener {
            signIn()
        }

        binding.btnSignOut.setOnClickListener {
            authApi.signOut(this)
            binding.user.text = authApi.getUserName()
        }
    }

    private val register =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            handleSignInActivityResult(result)
        }

    private fun signIn() {
        val intent = authApi.getSignInIntent(this)
        register.launch(intent)
    }

    private fun handleSignInActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                authApi.signIn(result.data)
            } catch (e: ApiException) {
                Toast.makeText(this, "Sign In Failed: $e", Toast.LENGTH_SHORT).show()
            }

            binding.progressCircular.visibility = VISIBLE

            // TODO check if coroutine code makes sense
            val authActivityContext = this
            CoroutineScope(Dispatchers.Main.immediate).launch {
                while (!authApi.isSignedIn()) {
                    delay(500)
                }

                Toast.makeText(
                    authActivityContext,
                    "Sign In Complete! ${authApi.isSignedIn()}",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(authActivityContext, MainActivity::class.java))
            }
        }
    }
}
