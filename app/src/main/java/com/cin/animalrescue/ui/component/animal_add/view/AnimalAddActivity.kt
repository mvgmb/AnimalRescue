package com.cin.animalrescue.ui.component.animal_add.view

import androidx.activity.viewModels
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalAddBinding
import com.cin.animalrescue.ui.component.animal_add.viewmodel.AnimalAddViewModel
import com.task.ui.base.BaseActivity
import java.util.*

class AnimalAddActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimalAddBinding
    private val animalListViewModel: AnimalAddViewModel by viewModels()
    private var i: Int = 0

    override fun initViewBinding() {
        binding = ActivityAnimalAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { handleBtnRegisterClick() }

        binding.btnTest.setOnClickListener {
            val id = UUID.randomUUID().toString()
            val smallID = id.substring(0, 5)
            animalListViewModel.insert(
                Animal(
                    id = id,
                    "helper_$smallID",
                    "type_$smallID",
                    "title_$smallID",
                    "location_$smallID",
                    "info_$smallID"
                )
            )
        }
    }

    private fun handleBtnRegisterClick() {
        animalListViewModel.insert(
            Animal(
                id = UUID.randomUUID().toString(),
                helper = "current_user",
                type = binding.type.text.toString(),
                title = binding.title.text.toString(),
                location = binding.location.text.toString(),
                info = binding.info.text.toString(),
            )
        )
        finish()
    }

    override fun observeViewModel() {}
}
