package com.example.projectbeta.ui.view.Dashboard

import AutoSliderAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.projectbeta.R
import com.example.projectbeta.databinding.FragmentDashboardBinding
import com.example.projectbeta.ui.adapter.FoodAdapter
import com.example.projectbeta.ui.viewmodel.FoodViewModel
import com.example.projectbeta.ui.viewmodel.NewsViewModel
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {
    private val newsViewModel: NewsViewModel by viewModels()
    private val foodViewModel: FoodViewModel by viewModels()
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var foodAdapter: FoodAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        setupRecyclerView()

        // Auto scroll for ViewPager2
        startAutoScroll(binding.autoSlider)

        // Observe food data
        viewLifecycleOwner.lifecycleScope.launch {
            foodViewModel.allFoods.collect { foods ->
                Log.d("DashboardFragment", "Received ${foods.size} foods")
                foodAdapter.submitList(foods)
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        foodAdapter = FoodAdapter()
        binding.recyclerViewFoodRecommendations.apply {
            adapter = foodAdapter
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    private fun startAutoScroll(viewPager: ViewPager2) {
        newsViewModel.healthyFoodNews.observe(viewLifecycleOwner) { articles ->
            val limitedArticles = articles.take(5)

            // Create adapter with click handler
            val autoSliderAdapter = AutoSliderAdapter(limitedArticles) { url ->
                navigateToWebView(url)
            }

            binding.autoSlider.adapter = autoSliderAdapter
            binding.dotsIndicator.attachTo(binding.autoSlider)
        }

        val handler = Handler(Looper.getMainLooper())
        val scrollRunnable = object : Runnable {
            override fun run() {
                val currentItem = viewPager.currentItem
                val totalItems = viewPager.adapter?.itemCount ?: 0

                if (totalItems > 0) {
                    viewPager.currentItem = (currentItem + 1) % totalItems
                }

                handler.postDelayed(this, 3000) // Auto-scroll every 3 seconds
            }
        }
        handler.postDelayed(scrollRunnable, 3000)
    }

    private fun navigateToWebView(url: String) {
        // Create bundle with URL
        val bundle = Bundle().apply {
            putString("url", url)
        }

        // Navigate to WebView fragment
        findNavController().navigate(
            R.id.action_dashboardFragment_to_webViewFragment,
            bundle
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
