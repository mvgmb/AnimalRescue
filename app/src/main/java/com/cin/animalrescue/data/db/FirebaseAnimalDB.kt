package com.cin.animalrescue.data.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cin.animalrescue.data.AnimalRepositorySource
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.ui.component.animal_list.view.AnimalListActivity
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

class FirebaseAnimalDB : AnimalRepositorySource {
    val db = Firebase.firestore

    var storage: FirebaseStorage = Firebase.storage

    override fun getById(id: String): LiveData<Animal> {
        val res = MutableLiveData<Animal>()
        db.collection("animals")
            .document(id)
            .get()
            .addOnSuccessListener {
                res.postValue(createAnimalFromData(it.data!!))
            }
            .addOnFailureListener { e ->
                Log.e("MY_TAG", "Error getting document by ID: $e")
            }
        return res
    }

    override fun getByType(type: String): LiveData<List<Animal>> {
        TODO("Not yet implemented")
    }

    override fun getAll(): LiveData<List<Animal>> {
        val res = MutableLiveData<List<Animal>>()
        db.collection("animals")
            .get()
            .addOnSuccessListener { result ->
                val animalMutableList = mutableListOf<Animal>()
                result.map {
                    animalMutableList.add(createAnimalFromData(it.data))
                }
                res.postValue(animalMutableList)
            }
            .addOnFailureListener { e ->
                Log.e("MY_TAG", "Error getting all documents: $e")
            }
        return res
    }

    override suspend fun insert(animal: Animal) {
        db.collection("animals")
            .document(animal.id)
            .set(animal)
            .addOnSuccessListener { documentReference ->
                Log.i("MY_TAG", "DocumentSnapshot added with ID: ${animal.id}")
            }
            .addOnFailureListener { e ->
                Log.e("MY_TAG", "Error inserting document: $e")
            }
    }

    override suspend fun update(animal: Animal) {
        TODO("Not yet implemented")
    }

    override suspend fun remove(animal: Animal) {
        TODO("Not yet implemented")
    }

    private fun createAnimalFromData(data: Map<String, Any>): Animal {
        return Animal(
            data["id"].toString(),
            data["helper"].toString(),
            data["type"].toString(),
            data["title"].toString(),
            data["location"].toString(),
            data["info"].toString(),
        )
    }
}
