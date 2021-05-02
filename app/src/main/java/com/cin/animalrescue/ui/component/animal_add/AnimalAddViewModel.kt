package com.cin.animalrescue.ui.component.animal_add

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.FirebaseAuthApi
import com.cin.animalrescue.data.db.FirebaseAnimalDB
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.data.repository.AnimalRepository
import com.cin.animalrescue.vo.Resource

class AnimalAddViewModel(application: Application) : AndroidViewModel(application) {
    private val authApi: AuthApi = FirebaseAuthApi()
    private val repo: AnimalRepository = AnimalRepository(FirebaseAnimalDB())

    fun getUserUID(): String? = authApi.getUserUID()
    fun insert(animal: Animal): LiveData<Resource<Boolean>> = repo.insert(animal)
}
