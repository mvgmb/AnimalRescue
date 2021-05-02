package com.cin.animalrescue.ui.component.animal_add

import android.app.Application
import android.net.Uri
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
    val animalImageURI : LiveData<Uri?> = _animalImageURI

    fun getUserUID(): String? = authApi.getUserUID()
    fun insert(animal: Animal): LiveData<Resource<Boolean>> = repo.insert(animal)

    fun setAnimalImageUri(uri: Uri) {
        _animalImageURI.postValue(uri)
    }

    fun refreshAnimalImageUri() {
        val tmp = _animalImageURI.value
        _animalImageURI.postValue(null)
        _animalImageURI.postValue(tmp)
    }
}
