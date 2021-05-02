package com.cin.animalrescue.ui.component.user

import android.content.Intent
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import com.cin.animalrescue.R
import com.cin.animalrescue.databinding.ActivityUserBinding
import com.cin.animalrescue.ui.component.main.MainActivity
import com.cin.animalrescue.utils.handleMenuItemClick
import com.task.ui.base.BaseActivity

class UserActivity : BaseActivity() {
    private val authViewModel: UserViewModel by viewModels()

    private lateinit var binding: ActivityUserBinding

    override fun initViewBinding() {
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setBindings() {
        binding.userName.text = authViewModel.getUserName()
        binding.userEmail.text = authViewModel.getUserEmail()

        binding.bottomNavigation.selectedItemId = R.id.user

        binding.bottomNavigation.post {
            setScrollViewViewMarginAsBottomNavHeight()
        }

        binding.btnSignOut.setOnClickListener {
            authViewModel.signOut(this)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

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

    override fun observeViewModel() {}
}
