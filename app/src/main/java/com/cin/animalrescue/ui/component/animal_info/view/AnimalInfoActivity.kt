package com.cin.animalrescue.ui.component.animal_info.view

import android.content.Intent
import androidx.activity.viewModels
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalInfoBinding
import com.cin.animalrescue.ui.component.animal_info.viewmodel.AnimalInfoViewModel
import com.cin.animalrescue.ui.component.map.view.MapsActivity
import com.cin.animalrescue.utils.observe
import com.task.ui.base.BaseActivity

class AnimalInfoActivity : BaseActivity() {
    private val animalInfoViewModel: AnimalInfoViewModel by viewModels()

    private lateinit var binding: ActivityAnimalInfoBinding
    private lateinit var id: String
    private lateinit var location: String

    override fun initViewBinding() {
        binding = ActivityAnimalInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()

        binding.btnMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
                .putExtra("location", location)
            startActivity(intent)
        }
    }

    override fun observeViewModel() {
        observe(animalInfoViewModel.getById(id), ::handleGetAnimalById)
    }

    private fun handleGetAnimalById(animal: Animal) {
        binding.title.text = animal.title
        binding.helper.text = animal.helper
        binding.type.text = animal.type
        binding.info.text = animal.info
        location = animal.location
        binding.location.text = location
    }
}
