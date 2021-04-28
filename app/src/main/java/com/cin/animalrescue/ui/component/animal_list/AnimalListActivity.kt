package com.cin.animalrescue.ui.component.animal_list

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.os.Looper.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalListBinding
import com.cin.animalrescue.utils.Logger
import com.cin.animalrescue.utils.observe
import com.cin.animalrescue.utils.observeOnce
import com.cin.animalrescue.vo.Resource
import com.google.android.gms.location.*
import com.task.ui.base.BaseActivity
import java.util.jar.Manifest

class AnimalListActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimalListBinding
    private lateinit var animalAdapter: AnimalAdapter

    private val PERMISSION_CODE = 1008
    private val animalListViewModel: AnimalListViewModel by viewModels()
    private var i: Int = 0
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

    override fun initViewBinding() {
        binding = ActivityAnimalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        animalAdapter = AnimalAdapter(layoutInflater)

        binding.rvAnimals.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = animalAdapter
        }
    }

    override fun observeViewModel() {
        observeOnce(animalListViewModel.getAll(), ::handleGetAllResult)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if(checkPermissions()) {
            if(isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location:Location? = task.result

                    if(location == null) {
                        getNewLocation()
                    } else {
                        binding.LocationTest.text = "your coordinates are lat:" + location.latitude + "long:" + location.longitude
                    }
                }
            } else {
                Toast.makeText(this, "Please enable your location service", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestPermission()
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            var lastLocation:Location = p0.lastLocation

            binding.LocationTest.text = "your coordinates are lat:" + lastLocation.latitude + "long:" + lastLocation.longitude

        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLocation() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest, locationCallback, myLooper()
        )
    }


    private fun checkPermissions():Boolean {
        if (
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        val permission = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
        requestPermissions(permission, PERMISSION_CODE)
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun handleGetAllResult(resource: Resource<List<Animal>>) {
        if (resource.isSuccess()) {
            val list = resource.data
            animalAdapter.submitList(list?.toList())
        } else {
            Logger.logError(resource.message.toString())

            // TODO improve image error to user
            Toast.makeText(
                this,
                "Failed to get all animals list: ${resource.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
