package com.cin.animalrescue.ui.component.animal_info.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cin.animalrescue.data.db.FirebaseAnimalDB
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.data.repository.AnimalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimalInfoViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AnimalRepository = AnimalRepository(FirebaseAnimalDB())

    fun getById(id: String): LiveData<Animal> {
        return repo.getById(id)
    }
}
