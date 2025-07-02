package com.example.projectbeta.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.projectbeta.R
import com.example.projectbeta.data.model.FoodRecommendation

class FoodRecommendationAdapter : RecyclerView.Adapter<FoodRecommendationAdapter.ViewHolder>() {

    private val recommendationList = mutableListOf<FoodRecommendation>()

    fun submitList(newList: List<FoodRecommendation>) {
        recommendationList.clear()
        recommendationList.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recommendation = recommendationList[position]
        // Set the meal type (e.g., Breakfast, Lunch, Dinner)
        holder.mealType.text = recommendation.meal

        // Set the food names (joined by a comma)
        val foodNames = recommendation.recommendedFoods.joinToString(", ") { it.name }
        holder.foodNames.text = foodNames
    }

    override fun getItemCount(): Int = recommendationList.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealType: TextView = view.findViewById(R.id.textViewMealType)
        val foodNames: TextView = view.findViewById(R.id.textViewFoodNames)
    }
}