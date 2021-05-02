package com.cin.animalrescue.ui.component.main

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.cin.animalrescue.databinding.ActivityMainBinding
import com.cin.animalrescue.ui.component.animal_list.AnimalListActivity
import com.cin.animalrescue.utils.observeOnce
import com.cin.animalrescue.vo.Resource
import com.task.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

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

    override fun onStart() {
        super.onStart()

        if (mainViewModel.isSignedIn()) {
            startActivity(Intent(this, AnimalListActivity::class.java))
            finish()
        }
    }

    override fun observeViewModel() {}

    private val registerForSignInActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::handleSignInActivityResult
    )

    private fun signIn() {
        val intent = mainViewModel.getSignInIntent(this)
        registerForSignInActivityResult.launch(intent)
    }

    private fun handleSignInActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            observeOnce(mainViewModel.signIn(result.data), ::handleSignInResult)
        } else {
            binding.progressCircular.visibility = View.INVISIBLE
            binding.btnSignIn.isEnabled = true
        }
    }

    private fun handleSignInResult(resource: Resource<Boolean>) {
        if (resource.isSuccess()) {
            Toast.makeText(this, "Sign In Complete!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, AnimalListActivity::class.java))
        } else {
            Toast.makeText(this, "Sign In Failed: ${resource.message}", Toast.LENGTH_SHORT).show()
            binding.progressCircular.visibility = View.INVISIBLE
            binding.btnSignIn.isEnabled = true
        }
    }
}
