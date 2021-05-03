package com.cin.animalrescue.ui.component.animal_list

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.cin.animalrescue.R
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalListBinding
import com.cin.animalrescue.utils.Logger
import com.cin.animalrescue.utils.handleMenuItemClick
import com.cin.animalrescue.utils.observeOnce
import com.cin.animalrescue.vo.Resource
import com.google.android.gms.location.*
import com.task.ui.base.BaseActivity

class AnimalListActivity : BaseActivity() {
    private val animalListViewModel: AnimalListViewModel by viewModels()

    private lateinit var binding: ActivityAnimalListBinding
    private lateinit var animalAdapter: AnimalAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun initViewBinding() {
        binding = ActivityAnimalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animalAdapter = AnimalAdapter(layoutInflater)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        getLocation()
    }

    override fun setBindings() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = animalAdapter
        }

        binding.bottomNavigation.post {
            setRecyclerViewMarginAsBottomNavHeight()
        }

        binding.bottomNavigation.selectedItemId = R.id.animal_list

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            handleMenuItemClick(this, item)
        }
    }

    private fun setRecyclerViewMarginAsBottomNavHeight() {
        val params = ConstraintLayout.LayoutParams(binding.recyclerView.layoutParams)
        params.setMargins(0, 0, 0, binding.bottomNavigation.height)
        binding.recyclerView.layoutParams = params
        binding.recyclerView.requestLayout()
    }

    override fun observeViewModel() {
        observeOnce(animalListViewModel.animalList, ::handleAnimalListChange)
    }

    private fun handleAnimalListChange(resource: Resource<List<Animal>>) {
        if (resource.isSuccess()) {
            val list = resource.data?.toMutableList()
            animalAdapter.submitList(list)
        } else {
            val msg = "Falha no carregamento da lista de animais: ${resource.message}"
            Logger.error(msg)
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val ACCESS_FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        private val ACCESS_COARSE_LOCATION_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION
        private val GET_LOCATION_REQUEST_CODE = 1001
        private val GET_LOCATION_PERMISSIONS = arrayOf(
            ACCESS_FINE_LOCATION_PERMISSION,
            ACCESS_COARSE_LOCATION_PERMISSION
        )
    }

    private fun getLocation() {
        requestPermissions(GET_LOCATION_PERMISSIONS, GET_LOCATION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GET_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        fusedLocationProviderClient.lastLocation
                            .addOnSuccessListener {
                                animalListViewModel.updateAnimalListOrderedByDistance(it.latitude, it.longitude)
                            }
                            .addOnFailureListener { e ->
                                Logger.error(e.toString())
                            }
                    } catch (e: SecurityException) {
                        Logger.error(e.toString())
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
