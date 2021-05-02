package com.cin.animalrescue.ui.component.main

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.FirebaseAuthApi
import com.cin.animalrescue.databinding.ActivityMainBinding
import com.cin.animalrescue.ui.component.animal_list.AnimalListActivity
import com.cin.animalrescue.utils.Logger
import com.google.android.gms.common.api.ApiException
import com.task.ui.base.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var authApi: AuthApi = FirebaseAuthApi()

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setBindings() {
        binding.btnSignIn.setOnClickListener {
            binding.progressCircular.visibility = View.VISIBLE
            binding.btnSignIn.isEnabled = false
            signIn()
        }
    }

    override fun observeViewModel() {}

    override fun onStart() {
        super.onStart()

        if (authApi.isSignedIn()) {
            startActivity(Intent(this, AnimalListActivity::class.java))
            finish()
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
                binding.progressCircular.visibility = View.INVISIBLE
                binding.btnSignIn.isEnabled = true

                Toast.makeText(this, "Sign In Failed: $e", Toast.LENGTH_SHORT).show()
            }


            // TODO check if coroutine code makes sense
            val authActivityContext = this
            CoroutineScope(Dispatchers.Main.immediate).launch {
                while (!authApi.isSignedIn()) {
                    delay(500)
                }

                Toast.makeText(
                    authActivityContext,
                    "Sign In Complete!",
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(Intent(authActivityContext, AnimalListActivity::class.java))
            }
        } else {
            binding.progressCircular.visibility = View.INVISIBLE
            binding.btnSignIn.isEnabled = true
        }
    }
}
