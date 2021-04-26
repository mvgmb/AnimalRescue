package com.cin.animalrescue.data

import android.net.Uri
import androidx.lifecycle.LiveData
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.vo.Resource

interface AnimalRepositorySource {
    fun getById(id: String): LiveData<Resource<Animal>>
    fun getAnimalImage(id: String): LiveData<Resource<Uri>>
    fun getByType(type: String): LiveData<Resource<List<Animal>>>
    fun getAll(): LiveData<Resource<List<Animal>>>
    fun insert(animal: Animal): LiveData<Resource<Boolean>>
    fun remove(animal: Animal): LiveData<Resource<Boolean>>
    fun update(animal: Animal): LiveData<Resource<Boolean>>
}
