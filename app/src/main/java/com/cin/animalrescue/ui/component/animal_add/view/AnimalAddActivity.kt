package com.cin.animalrescue.ui.component.animal_add.view

import android.content.Intent
import androidx.activity.viewModels
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalAddBinding
import com.cin.animalrescue.ui.component.animal_add.viewmodel.AnimalAddViewModel
import com.cin.animalrescue.ui.component.signin.view.AuthActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.task.ui.base.BaseActivity
import java.util.*

class AnimalAddActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimalAddBinding
    private val animalListViewModel: AnimalAddViewModel by viewModels()
    private var i: Int = 0
    private var currentUser: FirebaseUser? = null
    private lateinit var currentUserUID: String

    override fun initViewBinding() {
        binding = ActivityAnimalAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO Remove Firebase direct call
        currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
        currentUserUID = currentUser!!.uid

        binding.btnRegister.setOnClickListener { handleBtnRegisterClick() }

        binding.btnTest.setOnClickListener {
            val id = UUID.randomUUID().toString()
            val smallID = id.substring(0, 5)
            animalListViewModel.insert(
                Animal(
                    id = id,
                    helper_uid = currentUserUID,
                    type = "type_$smallID",
                    title = "title_$smallID",
                    location = "location_$smallID",
                    info = "info_$smallID"
                )
            )
        }
    }

    private fun handleBtnRegisterClick() {
        animalListViewModel.insert(
            Animal(
                id = UUID.randomUUID().toString(),
                helper_uid = currentUserUID,
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
