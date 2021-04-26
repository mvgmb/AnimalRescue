package com.cin.animalrescue.ui.component.animal_info

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.viewModels
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalInfoBinding
import com.cin.animalrescue.ui.component.map.MapsActivity
import com.cin.animalrescue.utils.Logger
import com.cin.animalrescue.utils.observeOnce
import com.cin.animalrescue.vo.Resource
import com.task.ui.base.BaseActivity

class AnimalInfoActivity : BaseActivity() {
    private val animalInfoViewModel: AnimalInfoViewModel by viewModels()

    private lateinit var binding: ActivityAnimalInfoBinding
    private lateinit var id: String
    private lateinit var location: String
    private lateinit var latitude: String
    private lateinit var longitude: String

    override fun initViewBinding() {
        binding = ActivityAnimalInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()

        binding.btnMaps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
                .putExtra("location", location)
                .putExtra("latitude", latitude)
                .putExtra("longitude", longitude)
            startActivity(intent)
        }
    }

    override fun observeViewModel() {
        observeOnce(animalInfoViewModel.getById(id), ::handleGetAnimalById)
        observeOnce(animalInfoViewModel.getAnimalImage(id), ::handleGetAnimalImage)
    }

    private fun handleGetAnimalById(resource: Resource<Animal>) {
        if (resource.isSuccess()) {
            val animal = resource.data!!

            location = animal.location
            latitude = animal.latitude.toString()
            longitude = animal.longitude.toString()

            binding.title.text = animal.title
            binding.helperUid.text = animal.helper_uid
            binding.type.text = animal.type
            binding.info.text = animal.info
            binding.location.text = location
        }
    }

    private fun handleGetAnimalImage(resource: Resource<Uri>) {
        if (resource.isSuccess()) {
            binding.imageView.setImageURI(resource.data)
        } else {
            Logger.logError(resource.message.toString())

            // TODO improve image error to user
            Toast.makeText(
                this,
                "Failed to load animal image ${resource.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
