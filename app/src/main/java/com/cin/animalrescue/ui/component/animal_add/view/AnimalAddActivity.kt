package com.cin.animalrescue.ui.component.animal_add.view

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalAddBinding
import com.cin.animalrescue.ui.component.animal_add.viewmodel.AnimalAddViewModel
import com.cin.animalrescue.ui.component.signin.view.AuthActivity
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
    var image_uri: Uri? = null

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
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (
                    checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    // Permission not enabled
                    var permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    // Show Popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                }
                else{
                    // Permission already granted
                    openCamera()
                }
            } else {
                    // system os is < marshmallow
                openCamera()
            }
        }

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
                    info = "info_$smallID"
                )
            )
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission from popup was granted
                    openCamera()
                } else {
                    // permission from popup was denied
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // called when image was captured
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            // set image captured
            binding.imageView.setImageURI(image_uri)
        }
    }

    private fun handleBtnRegisterClick() {
        val inputLocation = binding.location.text.toString()
        val geocode = Geocoder(this, Locale.getDefault())
        try {
            val result = geocode.getFromLocationName(inputLocation, 1)

            if (result == null || result.size == 0) {
                Toast.makeText(this, "Location '$inputLocation' not found", Toast.LENGTH_LONG).show()
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
                )
            )
            finish()

        } catch (e: Exception) {
            Toast.makeText(this, "Location exception: $e", Toast.LENGTH_LONG).show()
        }
    }

    override fun observeViewModel() {}
}
