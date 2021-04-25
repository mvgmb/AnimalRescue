package com.cin.animalrescue.ui.component.animal_add

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalAddBinding
import com.cin.animalrescue.ui.component.signin.AuthActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.task.ui.base.BaseActivity
import java.lang.Exception
import java.util.*

class AnimalAddActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimalAddBinding
    private val animalListViewModel: AnimalAddViewModel by viewModels()
    private var i: Int = 0
    private var currentUser: FirebaseUser? = null
    private lateinit var currentUserUID: String
    private val PERMISSION_CODE = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    private var imageUri: Uri? = null

    override fun initViewBinding() {
        binding = ActivityAnimalAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO Remove Firebase direct call
        currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
        currentUserUID = currentUser!!.uid

        binding.btnRegister.setOnClickListener { handleBtnRegisterClick() }

        binding.btnTakePicture.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
            ) {
                val permission = arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                openCamera()
            }
        }

        // TODO remove when finish project
        binding.btnTest.setOnClickListener {
            val id = UUID.randomUUID().toString()
            val smallID = id.substring(0, 5)
            animalListViewModel.insert(
                Animal(
                    id = id,
                    helper_uid = currentUserUID,
                    type = "type_$smallID",
                    title = "title_$smallID",
                    location = "location_$smallID",
                    latitude = -34.0,
                    longitude = 151.0,
                    info = "info_$smallID",
                    image_uri = imageUri.toString(),
                )
            )
        }
    }

    override fun observeViewModel() {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // called when image was captured
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // set image captured
            Log.i("TAGTAG", imageUri.toString())
            binding.imageView.setImageURI(imageUri)
        }
    }

    private fun handleBtnRegisterClick() {
        val inputLocation = binding.location.text.toString()
        val geocode = Geocoder(this, Locale.getDefault())
        try {
            val result = geocode.getFromLocationName(inputLocation, 1)

            if (result == null || result.size == 0) {
                Toast.makeText(
                    this,
                    "Location '$inputLocation' not found",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            animalListViewModel.insert(
                Animal(
                    id = UUID.randomUUID().toString(),
                    helper_uid = currentUserUID,
                    type = binding.type.text.toString(),
                    title = binding.title.text.toString(),
                    location = result[0].getAddressLine(0),
                    latitude = result[0].latitude,
                    longitude = result[0].longitude,
                    info = binding.info.text.toString(),
                    image_uri = imageUri.toString(),
                )
            )
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Location exception: $e", Toast.LENGTH_LONG).show()
        }
    }
}
