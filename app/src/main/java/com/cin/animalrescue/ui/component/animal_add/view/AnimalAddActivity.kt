package com.cin.animalrescue.ui.component.animal_add.view

import androidx.activity.viewModels
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalAddBinding
import com.cin.animalrescue.ui.component.animal_add.viewmodel.AnimalAddViewModel
import com.cin.animalrescue.ui.component.animal_list.viewmodel.AnimalListViewModel
import com.task.ui.base.BaseActivity

class AnimalAddActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimalAddBinding
    private val animalListViewModel: AnimalAddViewModel by viewModels()
    private var i: Int = 0

    override fun initViewBinding() {
        binding = ActivityAnimalAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTest.setOnClickListener {
            i += 1
            animalListViewModel.insert(
                Animal(
                    "id_$i",
                    "helper_$i",
                    "type_$i",
                    "title_$i",
                    "location_$i",
                    "info_$i"
                )
            )
        }
    }
}
