package com.cin.animalrescue.ui.component.animal_list.view

import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalListBinding
import com.cin.animalrescue.ui.component.animal_list.adapter.AnimalAdapter
import com.cin.animalrescue.ui.component.animal_list.viewmodel.AnimalListViewModel
import com.task.ui.base.BaseActivity

class AnimalListActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimalListBinding

    private val animalListViewModel: AnimalListViewModel by viewModels()
    private var i: Int = 0

    override fun initViewBinding() {
        binding = ActivityAnimalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animalAdapter = AnimalAdapter(layoutInflater)

        binding.rvAnimals.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = animalAdapter
        }

        animalListViewModel.getAll().observe(
            this,
            Observer {
                animalAdapter.submitList(it.toList())
            }
        )
    }
}
