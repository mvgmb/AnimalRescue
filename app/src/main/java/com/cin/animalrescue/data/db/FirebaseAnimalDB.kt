package com.cin.animalrescue.data.db

import android.net.Uri
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
        val file = Uri.parse(animal.image_uri)
        val storageRef = storage.reference.child("images/${animal.id}")

        storageRef.putFile(file)
            .addOnSuccessListener { taskSnapshot ->
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
            .addOnFailureListener { e ->
                Log.e("MY_TAG", e.toString())
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
            id = data["id"].toString(),
            helper_uid = data["helper_uid"].toString(),
            type = data["type"].toString(),
            title = data["title"].toString(),
            location = data["location"].toString(),
            latitude = data["latitude"].toString().toDouble(),
            longitude = data["longitude"].toString().toDouble(),
            info = data["info"].toString(),
            image_uri = data["image_uri"].toString(),
        )
    }
}
