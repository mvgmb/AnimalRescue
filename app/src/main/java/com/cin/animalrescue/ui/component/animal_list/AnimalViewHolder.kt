package com.cin.animalrescue.ui.component.animal_list

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.AnimalCardBinding
import com.cin.animalrescue.ui.component.animal_info.AnimalInfoActivity

class AnimalViewHolder(private val binding: AnimalCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var id: String = "id_example"
    var title: String = "title_example"
    var location: String = "location_example"

    init {
        binding.root.setOnClickListener {
            val ctx = binding.title.context
            val intent = Intent(ctx, AnimalInfoActivity::class.java)
                .putExtra("id", id)
            ctx.startActivity(intent)
        }
    }

    fun bindTo(animal: Animal) {
        id = animal.id
        title = animal.title
        location = animal.location

        binding.title.text = animal.title
        binding.location.text = animal.location
    }
}
