package com.example.projectbeta.ui.view.FoodRecommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projectbeta.data.local.entity.FoodEntity
import com.example.projectbeta.databinding.FragmentFoodRecommendationBinding
import com.example.projectbeta.ui.adapter.FoodAdapter
import com.example.projectbeta.ui.viewmodel.NutritionViewModel
import kotlin.random.Random

class FoodRecommendationFragment : Fragment() {
    private var _binding: FragmentFoodRecommendationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NutritionViewModel by viewModels {
        NutritionViewModel.Factory(requireActivity().application)
    }

    private val breakfastAdapter = FoodAdapter()
    private val lunchAdapter = FoodAdapter()
    private val dinnerAdapter = FoodAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupObservers()
        setupSubmitButton()
    }

    private fun setupRecyclerViews() {
        binding.recyclerViewBreakfast.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = breakfastAdapter
        }

        binding.recyclerViewLunch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = lunchAdapter
        }

        binding.recyclerViewDinner.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dinnerAdapter
        }
    }

    private fun setupObservers() {
        // Menggunakan foodEntityRecommendations yang baru
        viewModel.foodEntityRecommendations.observe(viewLifecycleOwner) { recommendations ->
            recommendations?.forEach { recommendation ->
                when (recommendation.meal) {
                    "Breakfast" -> breakfastAdapter.submitList(recommendation.foods)
                    "Lunch" -> lunchAdapter.submitList(recommendation.foods)
                    "Dinner" -> dinnerAdapter.submitList(recommendation.foods)
                }
            }
        }
    }

    private fun setupSubmitButton() {
        binding.buttonSubmit.setOnClickListener {
            val targetCalories = binding.inputCalories.text.toString().toDoubleOrNull()
            if (targetCalories == null || targetCalories <= 0) {
                binding.inputCalories.error = "Please enter a valid calorie amount"
                return@setOnClickListener
            }
            viewModel.recommendFood(targetCalories)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}