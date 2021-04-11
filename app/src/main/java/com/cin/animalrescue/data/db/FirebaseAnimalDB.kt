package com.cin.animalrescue.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cin.animalrescue.data.AnimalRepositorySource
import com.cin.animalrescue.data.model.Animal
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class FirebaseAnimalDB : AnimalRepositorySource {
    var storage: FirebaseStorage = Firebase.storage
    val db = Firebase.firestore

    override fun getAll(): LiveData<List<Animal>> {
        val res = MutableLiveData<List<Animal>>()
        db.collection("animals")
            .get()
            .addOnSuccessListener { result ->
                val animalMutableList = mutableListOf<Animal>()
                result.map { document ->
                    animalMutableList.add(
                        Animal(
                            document.data["id"].toString(),
                            document.data["helper"].toString(),
                            document.data["type"].toString(),
                            document.data["title"].toString(),
                            document.data["location"].toString(),
                            document.data["info"].toString(),
                        )
                    )
                }
                res.postValue(animalMutableList)
            }
            .addOnFailureListener { exception ->
                Log.e("MY_TAG", "Error getting documents.", exception)
            }
        return res
    }

    override suspend fun insert(animal: Animal) {
        db.collection("animals")
            .add(animal)
            .addOnSuccessListener { documentReference ->
                Log.i("MY_TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("MY_TAG", "Error adding document $e")
            }
    }

    override suspend fun update(animal: Animal) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(animal: Animal) {
        TODO("Not yet implemented")
    }

    override suspend fun getByType(type: String) {
        TODO("Not yet implemented")
    }
}
