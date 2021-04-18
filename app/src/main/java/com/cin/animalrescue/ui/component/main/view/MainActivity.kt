package com.cin.animalrescue.ui.component.main.view

import android.content.Intent
import android.widget.Toast
import com.cin.animalrescue.databinding.ActivityMainBinding
import com.cin.animalrescue.ui.component.animal_add.view.AnimalAddActivity
import com.cin.animalrescue.ui.component.animal_list.view.AnimalListActivity
import com.cin.animalrescue.ui.component.signin.view.AuthActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.task.ui.base.BaseActivity


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        Toast.makeText(this, "User: ${auth.currentUser?.uid}", Toast.LENGTH_SHORT).show()
        if (auth.currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

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

}
