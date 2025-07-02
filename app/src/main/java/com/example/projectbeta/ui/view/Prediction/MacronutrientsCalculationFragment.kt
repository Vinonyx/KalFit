package com.example.projectbeta.ui.view.Prediction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.projectbeta.ui.adapter.SpinnerAdapter
import com.example.projectbeta.ui.viewmodel.NutritionViewModel
import com.example.projectbeta.R
import com.example.projectbeta.databinding.FragmentMacronutrientsCalculationBinding

class MacronutrientsCalculationFragment : Fragment() {
    private var _binding: FragmentMacronutrientsCalculationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NutritionViewModel by viewModels()
    private var selectedGoal: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMacronutrientsCalculationBinding.inflate(inflater, container, false)

        // Setup spinner
        SpinnerAdapter.setupSpinner(
            fragment = this,
            spinner = binding.spinnerGoals,
            labelsResId = R.array.goal_labels, // Array untuk label
            valuesResId = R.array.goal_values, // Array untuk value
        ) { selectedGoal ->
            // Simpan value yang dipilih
            this.selectedGoal = selectedGoal
        }


        // Observe nutrition response
        viewModel.nutritionResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
//                binding.textViewCalories.text = "Kalori: ${it.target_calories} kcal"
                binding.textViewProtein.text = "Protein: ${it.protein}g"
                binding.textViewFat.text = "Lemak: ${it.fat}g"
                binding.textViewCarbs.text = "Karbohidrat: ${it.carbohydrate}g"
            }
        }

        binding.buttonSubmit.setOnClickListener {
            val weight = binding.inputWeight.text.toString().toDoubleOrNull() ?: 0.0
            val height = binding.inputHeight.text.toString().toDoubleOrNull() ?: 0.0
            val age = binding.inputAge.text.toString().toIntOrNull() ?: 0

            if (weight > 0 && height > 0 && age > 0 && selectedGoal.isNotBlank()) {
                Log.d("MacronutrientsCalculationFragment", "Calculating for weight=$weight, height=$height, age=$age, goal=$selectedGoal")
                viewModel.predictCalories(weight, height, age, selectedGoal)
            } else {
                Log.d("MacronutrientsCalculationFragment", "Invalid input")
            }
        }

        return binding.root
    }

//    // Save selected goal
//    fun onGoalSelected(goal: String) {
//        selectedGoal = goal
//    }
}
