package com.cin.animalrescue.ui.component.animal_add.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cin.animalrescue.data.db.FirebaseAnimalDB
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.data.repository.AnimalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimalAddViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AnimalRepository = AnimalRepository(FirebaseAnimalDB())

    fun getAll(): LiveData<List<Animal>> {
        return repo.getAll()
    }

    fun insert(animal: Animal) {
        viewModelScope.launch(Dispatchers.Default) {
            repo.insert(animal)
        }
    }

    fun update(animal: Animal) {
        viewModelScope.launch(Dispatchers.Default) {
            repo.update(animal)
        }
    }

    fun remove(animal: Animal) {
        viewModelScope.launch(Dispatchers.Default) {
            repo.remove(animal)
        }
    }

    fun getByType(type: String) {
        viewModelScope.launch(Dispatchers.Default) {
            repo.getByType(type)
        }
    }
}