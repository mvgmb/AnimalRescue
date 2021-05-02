package com.cin.animalrescue.ui.component.animal_list

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
import com.task.ui.base.BaseActivity


class AnimalListActivity : BaseActivity() {
    private val animalListViewModel: AnimalListViewModel by viewModels()

    private lateinit var binding: ActivityAnimalListBinding
    private lateinit var animalAdapter: AnimalAdapter

    override fun initViewBinding() {
        binding = ActivityAnimalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animalAdapter = AnimalAdapter(layoutInflater)
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
        observeOnce(animalListViewModel.getAll(), ::handleGetAllResult)
    }

    private fun handleGetAllResult(resource: Resource<List<Animal>>) {
        if (resource.isSuccess()) {
            val list = resource.data?.toMutableList()
            list?.sortBy { it.title }
            animalAdapter.submitList(list)
        } else {
            Logger.error(resource.message.toString())

            // TODO improve image error to user
            Toast.makeText(
                this,
                "Failed to get all animals list: ${resource.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
