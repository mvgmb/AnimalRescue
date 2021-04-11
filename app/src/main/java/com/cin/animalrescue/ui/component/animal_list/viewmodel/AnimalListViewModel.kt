package com.cin.animalrescue.ui.component.animal_list.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.cin.animalrescue.data.db.FirebaseAnimalDB
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.data.repository.AnimalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AnimalListViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AnimalRepository = AnimalRepository(FirebaseAnimalDB())

    fun getAll(): LiveData<List<Animal>> {
        return repo.getAll()
    }
    
    fun getByType(type: String) {
        viewModelScope.launch(Dispatchers.Default) {
            repo.getByType(type)
        }
    }
}
