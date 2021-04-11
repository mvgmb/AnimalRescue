package com.cin.animalrescue.ui.component.main.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.cin.animalrescue.R
import com.cin.animalrescue.databinding.ActivityMainBinding
import com.cin.animalrescue.ui.component.animal_add.view.AnimalAddActivity
import com.cin.animalrescue.ui.component.animal_list.view.AnimalListActivity
import com.task.ui.base.BaseActivity


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAnimalList.setOnClickListener {
            startActivity(Intent(this, AnimalListActivity::class.java))
        }

        binding.btnAddAnimal.setOnClickListener {
            startActivity(Intent(this, AnimalAddActivity::class.java))
        }
    }
}
