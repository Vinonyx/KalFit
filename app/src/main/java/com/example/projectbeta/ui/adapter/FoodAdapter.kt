package com.example.projectbeta.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectbeta.data.local.entity.FoodEntity
import com.example.projectbeta.databinding.ItemFoodBinding
import androidx.recyclerview.widget.ListAdapter

class FoodAdapter : ListAdapter<FoodEntity, FoodAdapter.FoodViewHolder>(FoodDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FoodViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(food: FoodEntity) {
            binding.apply {
                textViewName.text = food.name
                textViewCalories.text = "${food.calories} kcal"
                textViewProtein.text = "${food.proteins}g protein"
                textViewCarbs.text = "${food.carbohydrate}g carbs"
                textViewFat.text = "${food.fat}g fat"

                // Load image using Glide or other image loading library
                Glide.with(itemView)
                    .load(food.image)
                    .into(imageViewFood)
            }
        }
    }

    private class FoodDiffCallback : DiffUtil.ItemCallback<FoodEntity>() {
        override fun areItemsTheSame(oldItem: FoodEntity, newItem: FoodEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodEntity, newItem: FoodEntity): Boolean {
            return oldItem == newItem
        }
    }
}