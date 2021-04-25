package com.cin.animalrescue.ui.component.animal_info

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalInfoBinding
import com.cin.animalrescue.ui.component.map.MapsActivity
import com.cin.animalrescue.utils.observe
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.task.ui.base.BaseActivity
import java.io.File

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
        observe(animalInfoViewModel.getById(id), ::handleGetAnimalById)
    }

    private fun handleGetAnimalById(animal: Animal) {
        val storageReference = Firebase.storage.reference.child("images/${animal.id}")
        val localFile = File.createTempFile(animal.id, "jpg")
        storageReference.getFile(localFile).addOnSuccessListener {
            binding.imageView.setImageURI(Uri.fromFile(localFile))
        }.addOnFailureListener { e ->
            Log.e("MY_TAG", e.toString())
        }

        binding.title.text = animal.title
        binding.helperUid.text = animal.helper_uid
        binding.type.text = animal.type
        binding.info.text = animal.info
        location = animal.location
        latitude = animal.latitude.toString()
        longitude = animal.longitude.toString()
        binding.location.text = location
    }
}
