package com.cin.animalrescue.ui.component.animal_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cin.animalrescue.data.db.FirebaseAnimalDB
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.data.repository.AnimalRepository
import com.cin.animalrescue.vo.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

class AnimalListViewModel(application: Application) : AndroidViewModel(application) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val repo: AnimalRepository = AnimalRepository(FirebaseAnimalDB())

    private val _animalList = MutableLiveData<Resource<List<Animal>>>()
    val animalList: LiveData<Resource<List<Animal>>> = _animalList

    fun updateAnimalListOrderedByDistance(lat: Double, long: Double) {
        coroutineScope.launch {
            val resource = repo.getAll()
            val list = resource.data?.toMutableList()
            list?.sortBy {
                dist(lat, long, it.latitude, it.longitude)
            }
            _animalList.postValue(Resource.success(list))
        }
    }

    private fun dist(
        lat_a: Double,
        lng_a: Double,
        lat_b: Double,
        lng_b: Double
    ): Double {
        val pk = (180f / Math.PI).toFloat()
        val a1 = lat_a / pk
        val a2 = lng_a / pk
        val b1 = lat_b / pk
        val b2 = lng_b / pk
        val t1 = cos(a1) * cos(a2) * cos(b1) * cos(b2)
        val t2 = cos(a1) * sin(a2) * cos(b1) * sin(b2)
        val t3 = sin(a1) * sin(b1)
        return acos(t1 + t2 + t3)
    }
}
