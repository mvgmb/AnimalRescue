package com.cin.animalrescue.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cin.animalrescue.data.AnimalRepositorySource
import com.cin.animalrescue.data.model.Animal

class AnimalRepository(private val animalRepositorySource: AnimalRepositorySource) {
    @WorkerThread
    fun getById(id: String): LiveData<Animal> {
        return animalRepositorySource.getById(id)
    }

    @WorkerThread
    fun getByType(type: String): LiveData<List<Animal>> {
        return animalRepositorySource.getByType(type)
    }

    @WorkerThread
    fun getAll(): LiveData<List<Animal>> {
        return animalRepositorySource.getAll()
    }

    @WorkerThread
    suspend fun insert(animal: Animal) {
        animalRepositorySource.insert(animal)
    }

    @WorkerThread
    suspend fun update(animal: Animal) {
        animalRepositorySource.update(animal)
    }

    @WorkerThread
    suspend fun remove(animal: Animal) {
        animalRepositorySource.remove(animal)
    }
}
