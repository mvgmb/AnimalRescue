package com.cin.animalrescue.ui.component.animal_add

import android.app.Application
import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.FirebaseAuthApi
import com.cin.animalrescue.data.db.FirebaseAnimalDB
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.data.repository.AnimalRepository
import com.cin.animalrescue.vo.Resource

class AnimalAddViewModel(application: Application) : AndroidViewModel(application) {
    private val authApi: AuthApi = FirebaseAuthApi()
    private val repo: AnimalRepository = AnimalRepository(FirebaseAnimalDB())

    private val _animalImageURI = MutableLiveData<Uri?>(null)
    val animalImageURI: LiveData<Uri?> = _animalImageURI

    fun getUserUID(): String? = authApi.getUserUID()
    fun insert(animal: Animal): LiveData<Resource<Boolean>> = repo.insert(animal)

    fun createLocalAnimalImage(c: ContentResolver): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        val imageUri = c.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        _animalImageURI.postValue(imageUri)
        return imageUri
    }

    fun setAnimalImageUri(uri: Uri) {
        _animalImageURI.postValue(uri)
    }

    fun refreshAnimalImageUri() {
        _animalImageURI.postValue(_animalImageURI.value)
    }
}
