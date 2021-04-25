package com.cin.animalrescue.ui.component.main

import android.content.Intent
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.FirebaseAuthApi
import com.cin.animalrescue.databinding.ActivityMainBinding
import com.cin.animalrescue.ui.component.animal_add.AnimalAddActivity
import com.cin.animalrescue.ui.component.animal_list.AnimalListActivity
import com.cin.animalrescue.ui.component.signin.AuthActivity
import com.task.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var authApi: AuthApi

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authApi = FirebaseAuthApi()

        binding.btnAnimalList.setOnClickListener {
            startActivity(Intent(this, AnimalListActivity::class.java))
        }

        binding.btnAddAnimal.setOnClickListener {
            startActivity(Intent(this, AnimalAddActivity::class.java))
        }

        binding.btnAuth.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
        }
    }

    override fun observeViewModel() {}

    override fun onStart() {
        super.onStart()

        if (!authApi.isSignedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
