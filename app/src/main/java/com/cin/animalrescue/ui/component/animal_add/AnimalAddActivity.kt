package com.cin.animalrescue.ui.component.animal_add

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import com.cin.animalrescue.R
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalAddBinding
import com.cin.animalrescue.utils.Logger
import com.cin.animalrescue.utils.handleMenuItemClick
import com.cin.animalrescue.utils.observe
import com.cin.animalrescue.utils.observeOnce
import com.task.ui.base.BaseActivity
import java.util.*

class AnimalAddActivity : BaseActivity() {
    private val animalListViewModel: AnimalAddViewModel by viewModels()

    private lateinit var binding: ActivityAnimalAddBinding

    companion object {
        private val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private val WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private val OPEN_CAMERA_REQUEST_CODE = 1000
        private val OPEN_CAMERA_PERMISSIONS = arrayOf(
            CAMERA_PERMISSION,
            WRITE_EXTERNAL_STORAGE_PERMISSION
        )
    }

    override fun initViewBinding() {
        binding = ActivityAnimalAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setBindings() {
        binding.btnRegister.setOnClickListener {
            handleBtnRegisterClick()
        }

        binding.btnTakePicture.setOnClickListener {
            requestOpenCamera()
        }

        binding.bottomNavigation.post {
            setScrollViewViewMarginAsBottomNavHeight()
        }

        binding.bottomNavigation.selectedItemId = R.id.animal_add

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            handleMenuItemClick(this, item)
        }
    }

    private fun setScrollViewViewMarginAsBottomNavHeight() {
        val params = ConstraintLayout.LayoutParams(binding.scrollView.layoutParams)
        params.setMargins(0, 0, 0, binding.bottomNavigation.height)
        binding.scrollView.layoutParams = params
        binding.scrollView.requestLayout()
    }

    override fun observeViewModel() {
        observe(animalListViewModel.animalImageURI) {
            binding.imageView.setImageURI(it)
        }
    }

    private fun requestOpenCamera() {
        requestPermissions(OPEN_CAMERA_PERMISSIONS, OPEN_CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            OPEN_CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    Toast.makeText(this, "Permissão Negada", Toast.LENGTH_LONG).show()
                }
            }
            else -> {
                Toast.makeText(this, "Houve algum erro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val registerForCameraActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::handleCameraActivityResult
    )

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the camera")
        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        animalListViewModel.setAnimalImageUri(imageUri!!)

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        registerForCameraActivityResult.launch(cameraIntent)
    }

    private fun handleCameraActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            animalListViewModel.refreshAnimalImageUri()
        }
    }

    private fun handleBtnRegisterClick() {
        if (isAnyFieldEmpty()) {
            Toast.makeText(this, "Favor preencher todos os campos", Toast.LENGTH_LONG).show()
            return
        }

        try {
            val geocode = Geocoder(this, Locale.getDefault())
            val inputLocation = binding.location.text.toString()
            val result = geocode.getFromLocationName(inputLocation, 1)

            if (result == null || result.size == 0) {
                Logger.error("Location '$inputLocation' not found")
                Toast.makeText(
                    this,
                    "Localização '$inputLocation' não foi encontrada",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            insertAnimal(result)
        } catch (e: Exception) {
            Logger.error(e.toString())
            Toast.makeText(this, "Alguma coisa deu errada: $e", Toast.LENGTH_LONG).show()
        }
    }

    private fun isAnyFieldEmpty(): Boolean = binding.title.text.isEmpty() ||
            binding.type.text.isEmpty() ||
            binding.location.text.isEmpty() ||
            binding.info.text.isEmpty()

    private fun insertAnimal(result: List<Address>) {
        val animal = Animal(
            id = UUID.randomUUID().toString(),
            helper_uid = animalListViewModel.getUserUID().toString(),
            type = binding.type.text.toString(),
            title = binding.title.text.toString(),
            location = result[0].getAddressLine(0),
            latitude = result[0].latitude,
            longitude = result[0].longitude,
            info = binding.info.text.toString(),
            image_uri = animalListViewModel.animalImageURI.toString(),
        )
        observeOnce(animalListViewModel.insert(animal)) { resource ->
            if (resource.isSuccess()) {
                finish()
            } else {
                Logger.error(resource.message.toString())
                Toast.makeText(this, resource.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
