package com.example.projectbeta.ui.adapter

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.projectbeta.R

object SpinnerAdapter {
    // Fungsi untuk mengatur Spinner dengan label dan nilai
    fun setupSpinner(
        fragment: Fragment,
        spinner: Spinner,
        labelsResId: Int,
        valuesResId: Int,
        onItemSelected: (String) -> Unit // Callback untuk menangani item yang dipilih
    ) {
        val context = fragment.requireContext()
        val goalLabels = context.resources.getStringArray(labelsResId) // Labels untuk ditampilkan
        val goalValues = context.resources.getStringArray(valuesResId) // Values untuk logika aplikasi

        // Atur adapter spinner
        val adapter = ArrayAdapter(
            context,
            R.layout.spinner_item, // Layout custom item spinner
            goalLabels
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinner.adapter = adapter

        // Set listener untuk menangani pilihan
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                // Callback dengan value yang sesuai
                onItemSelected(goalValues[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Tidak ada aksi jika tidak ada yang dipilih
            }
        }
    }
}
