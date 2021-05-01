package com.cin.animalrescue.ui.component.animal_list

import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cin.animalrescue.R
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.ActivityAnimalListBinding
import com.cin.animalrescue.utils.Logger
import com.cin.animalrescue.utils.handleMenuItemClick
import com.cin.animalrescue.utils.observeOnce
import com.cin.animalrescue.vo.Resource
import com.task.ui.base.BaseActivity
import java.lang.Exception


class AnimalListActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimalListBinding
    private lateinit var animalAdapter: AnimalAdapter

    private val animalListViewModel: AnimalListViewModel by viewModels()
    private var i: Int = 0

    override fun initViewBinding() {
        binding = ActivityAnimalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animalAdapter = AnimalAdapter(layoutInflater)

        binding.bottomNavigation.selectedItemId = R.id.animal_list

        binding.bottomNavigation.post{
            setRecyclerViewMarginAsBottomNavHeight()
        }

        binding.rvAnimals.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = animalAdapter
        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            handleMenuItemClick(this, item)
        }
    }

    fun setRecyclerViewMarginAsBottomNavHeight() {
        val marginLayoutParams = FrameLayout.LayoutParams(binding.rvAnimals.layoutParams)
        marginLayoutParams.setMargins(0, 0, 0, binding.bottomNavigation.height)
        binding.rvAnimals.layoutParams = marginLayoutParams
        binding.rvAnimals.requestLayout()
    }

    override fun observeViewModel() {
        observeOnce(animalListViewModel.getAll(), ::handleGetAllResult)
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
