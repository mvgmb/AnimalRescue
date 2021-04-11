package com.cin.animalrescue.ui.component.animal_info.view

import com.cin.animalrescue.databinding.ActivityAnimalInfoBinding
import com.task.ui.base.BaseActivity

class AnimalInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityAnimalInfoBinding

    override fun initViewBinding() {
        binding = ActivityAnimalInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = intent.getStringExtra("id")
    }

    override fun observeViewModel() {}
}
