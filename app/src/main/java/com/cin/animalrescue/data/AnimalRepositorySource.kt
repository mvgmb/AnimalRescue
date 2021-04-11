package com.cin.animalrescue.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.cin.animalrescue.data.model.Animal
import kotlinx.coroutines.flow.Flow

interface AnimalRepositorySource {
    fun getAll(): LiveData<List<Animal>>
    suspend fun insert(animal: Animal)
    suspend fun update(animal: Animal)
    suspend fun remove(animal: Animal)
    suspend fun getByType(type: String)
}