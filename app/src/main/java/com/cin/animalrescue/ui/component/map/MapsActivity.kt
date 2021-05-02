package com.cin.animalrescue.ui.component.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cin.animalrescue.R
import com.cin.animalrescue.utils.Logger
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var location: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        location = intent.getStringExtra("location").toString()

        try {
            latitude = intent.getStringExtra("latitude").toString().toDouble()
            longitude = intent.getStringExtra("longitude").toString().toDouble()
        } catch (e: Exception) {
            Logger.error(e.toString())
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val position = LatLng(latitude, longitude)
        val marker = MarkerOptions()
            .position(position)
            .title(location)
        val cameraPosition = CameraUpdateFactory.newLatLngZoom(position, 18.0f)

        googleMap.addMarker(marker)
        googleMap.moveCamera(cameraPosition)
    }
}
