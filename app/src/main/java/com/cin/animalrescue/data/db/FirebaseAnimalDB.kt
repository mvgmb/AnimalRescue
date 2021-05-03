package com.cin.animalrescue.data.db

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cin.animalrescue.data.AnimalRepositorySource
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.vo.Resource
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File

class FirebaseAnimalDB : AnimalRepositorySource {
    private val db = Firebase.firestore
    private val storage: FirebaseStorage = Firebase.storage

    override fun getById(id: String): LiveData<Resource<Animal>> {
        val res = MutableLiveData<Resource<Animal>>()

        db.collection("animals")
            .document(id)
            .get()
            .addOnSuccessListener {
                val animal = createAnimalFromData(it.data!!)
                res.postValue(Resource.success(animal))
            }
            .addOnFailureListener { e ->
                res.postValue(Resource.error(e.toString(), null))
            }

        return res
    }

    override fun getByType(type: String): LiveData<Resource<List<Animal>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Resource<List<Animal>> {
        val res: Resource<List<Animal>>

        val task = db.collection("animals").get()

        try {
            await(task)
        } catch (e: Exception) {
            return Resource.error(e.toString(), null)
        }

        res = if (task.isSuccessful) {
            val animalMutableList = mutableListOf<Animal>()
            task.result?.map {
                animalMutableList.add(createAnimalFromData(it.data))
            }
            Resource.success(animalMutableList)
        } else {
            Resource.error(task.result.toString(), null)
        }
        return res
    }

    override fun getAnimalImage(id: String): LiveData<Resource<Uri>> {
        val res = MutableLiveData<Resource<Uri>>()

        val storageReference = Firebase.storage.reference.child("images/$id")
        val localFile = File.createTempFile(id, "jpg")
        storageReference.getFile(localFile)
            .addOnSuccessListener {
                val animalImageUri = Uri.fromFile(localFile)
                res.postValue(Resource.success(animalImageUri))
            }.addOnFailureListener { e ->
                res.postValue(Resource.error(e.toString(), null))
            }

        return res
    }

    override fun insert(animal: Animal): LiveData<Resource<Boolean>> {
        val res = MutableLiveData<Resource<Boolean>>()

        db.collection("animals")
            .document(animal.id)
            .set(animal)
            .addOnSuccessListener {
                uploadAnimalImage(animal, res)
            }
            .addOnFailureListener { e ->
                res.postValue(Resource.error("Failed to insert animal: $e", null))
            }

        return res
    }

    private fun uploadAnimalImage(animal: Animal, res: MutableLiveData<Resource<Boolean>>) {
        val file = Uri.parse(animal.image_uri)
        val storageRef = storage.reference.child("images/${animal.id}")
        storageRef.putFile(file)
            .addOnSuccessListener {
                res.postValue(Resource.success(true))
            }
            .addOnFailureListener { e ->
                res.postValue(Resource.error("Failed to upload animal image: $e", null))
            }
    }

    override fun update(animal: Animal): LiveData<Resource<Boolean>> {
        TODO("Not yet implemented")
    }

    override fun remove(animal: Animal): LiveData<Resource<Boolean>> {
        TODO("Not yet implemented")
    }

    private fun createAnimalFromData(data: Map<String, Any>): Animal {
        return Animal(
            id = data["id"].toString(),
            helper_uid = data["helper_uid"].toString(),
            helper_name = data["helper_name"].toString(),
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
