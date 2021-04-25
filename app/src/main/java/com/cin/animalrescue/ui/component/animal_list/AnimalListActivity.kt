package com.cin.animalrescue.ui.component.animal_list

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalListBinding
import com.cin.animalrescue.utils.observe
import com.task.ui.base.BaseActivity

class AnimalListActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimalListBinding
    private lateinit var animalAdapter: AnimalAdapter

    private val animalListViewModel: AnimalListViewModel by viewModels()
    private var i: Int = 0

    override fun initViewBinding() {
        binding = ActivityAnimalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animalAdapter = AnimalAdapter(layoutInflater)

        binding.rvAnimals.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = animalAdapter
        }
    }

    override fun observeViewModel() {
        observe(animalListViewModel.getAll(), ::handleGetAllResult)
    }

    private fun handleGetAllResult(list: List<Animal>) {
        animalAdapter.submitList(list.toList())
    }
}
