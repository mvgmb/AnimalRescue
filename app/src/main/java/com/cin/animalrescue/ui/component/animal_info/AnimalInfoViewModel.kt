package com.cin.animalrescue.ui.component.animal_info

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cin.animalrescue.data.db.FirebaseAnimalDB
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.data.repository.AnimalRepository

class AnimalInfoViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AnimalRepository = AnimalRepository(FirebaseAnimalDB())

    fun getById(id: String): LiveData<Animal> {
        return repo.getById(id)
    }
}
