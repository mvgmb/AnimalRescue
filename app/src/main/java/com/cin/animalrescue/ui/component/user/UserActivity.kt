package com.cin.animalrescue.ui.component.user

import android.content.Intent
import com.cin.animalrescue.R
import com.cin.animalrescue.data.AuthApi
import com.cin.animalrescue.data.auth.FirebaseAuthApi
import com.cin.animalrescue.databinding.ActivityUserBinding
import com.cin.animalrescue.ui.component.main.MainActivity
import com.cin.animalrescue.utils.handleMenuItemClick
import com.task.ui.base.BaseActivity

class UserActivity : BaseActivity() {
    private lateinit var binding: ActivityUserBinding
    private val authApi: AuthApi = FirebaseAuthApi()

    override fun initViewBinding() {
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBindings()
    }

    override fun setBindings() {
        // TODO add ViewModel
        binding.userName.text = authApi.getUserName()
        binding.userEmail.text = authApi.getUserEmail()

        binding.btnSignOut.setOnClickListener {
            authApi.signOut(this)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.bottomNavigation.selectedItemId = R.id.user
    }

    override fun onStart() {
        super.onStart()
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            handleMenuItemClick(this, item)
        }
    }

    override fun observeViewModel() {}
}
