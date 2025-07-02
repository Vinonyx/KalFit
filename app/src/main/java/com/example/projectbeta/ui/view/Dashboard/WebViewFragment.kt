package com.example.projectbeta.ui.view.Dashboard

import android.os.Bundle
import android.webkit.URLUtil
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.projectbeta.databinding.FragmentWebViewBinding

class WebViewFragment : Fragment() {
    private var _binding: FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get URL from arguments
        arguments?.getString("url")?.let { url ->
            setupWebView(url)
        }

        // Setup toolbar back button
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // Handle back press for WebView navigation
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : androidx.activity.OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack()
                    } else {
                        isEnabled = false // Matikan callback agar onBackPressed sistem bekerja
                        requireActivity().onBackPressed()
                    }
                }
            }
        )
    }

    private fun setupWebView(url: String) {
        if (URLUtil.isValidUrl(url)) {
            binding.webView.apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: android.webkit.WebView?,
                        url: String?
                    ): Boolean {
                        return if (url != null && URLUtil.isValidUrl(url)) {
                            view?.loadUrl(url)
                            true
                        } else {
                            false
                        }
                    }
                }
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                }
                loadUrl(url)
            }
        } else {
            binding.webView.loadData(
                "<html><body><h2>Invalid URL</h2></body></html>",
                "text/html",
                "UTF-8"
            )
        }
    }

    override fun onDestroyView() {
        binding.webView.apply {
            clearHistory()
            clearCache(true)
            loadUrl("about:blank")
            onPause()
            removeAllViews()
            destroy()
        }
        _binding = null
        super.onDestroyView()
    }
}
