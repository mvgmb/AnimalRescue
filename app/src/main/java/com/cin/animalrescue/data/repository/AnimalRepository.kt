package com.cin.animalrescue.data.repository

import android.net.Uri
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.cin.animalrescue.data.AnimalRepositorySource
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.vo.Resource

class AnimalRepository(private val animalRepositorySource: AnimalRepositorySource) {
    @WorkerThread
    fun getById(id: String): LiveData<Resource<Animal>> {
        return animalRepositorySource.getById(id)
    }

    @WorkerThread
    fun getAnimalImage(id: String): LiveData<Resource<Uri>> {
        return animalRepositorySource.getAnimalImage(id)
    }

    @WorkerThread
    fun getByType(type: String): LiveData<Resource<List<Animal>>> {
        return animalRepositorySource.getByType(type)
    }

    @WorkerThread
    fun getAll(): LiveData<Resource<List<Animal>>> {
        return animalRepositorySource.getAll()
    }

    @WorkerThread
    fun insert(animal: Animal): LiveData<Resource<Boolean>> {
        return animalRepositorySource.insert(animal)
    }

    @WorkerThread
    fun update(animal: Animal): LiveData<Resource<Boolean>> {
        return animalRepositorySource.update(animal)
    }

    @WorkerThread
    fun remove(animal: Animal): LiveData<Resource<Boolean>> {
        return animalRepositorySource.remove(animal)
    }
}
