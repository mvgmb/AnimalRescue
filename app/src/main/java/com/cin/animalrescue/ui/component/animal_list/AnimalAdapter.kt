package com.cin.animalrescue.ui.component.animal_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.cin.animalrescue.data.model.Animal
import com.cin.animalrescue.databinding.AnimalCardBinding

class AnimalAdapter(
    private val inflater: LayoutInflater
) : ListAdapter<Animal, AnimalViewHolder>(AnimalDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val binding = AnimalCardBinding.inflate(inflater, parent, false)
        return AnimalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    private object AnimalDiffer : DiffUtil.ItemCallback<Animal>() {
        override fun areItemsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Animal, newItem: Animal): Boolean {
            return oldItem == newItem
        }
    }
}
