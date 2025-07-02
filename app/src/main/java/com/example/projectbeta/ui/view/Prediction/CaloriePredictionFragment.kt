package com.example.projectbeta.ui.view.Prediction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projectbeta.R
import com.example.projectbeta.utils.TFLiteHelper
import com.example.projectbeta.databinding.FragmentCaloriePredictionBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.math.BigDecimal
import java.math.RoundingMode

class CaloriePredictionFragment : Fragment() {
    private lateinit var tfliteHelper: TFLiteHelper
    private lateinit var firestore: FirebaseFirestore

    private var _binding: FragmentCaloriePredictionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCaloriePredictionBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inisialisasi TFLiteHelper
        tfliteHelper = TFLiteHelper(requireContext(), "model.tflite")

        // Inisialisasi Firebase Firestore
        firestore = FirebaseFirestore.getInstance()

        // Tombol Submit untuk melakukan prediksi
        binding.buttonSubmit.setOnClickListener {
            try {
                // Ambil nilai input dari EditText
                val protein = binding.inputProtein.text.toString().toFloat()
                val fat = binding.inputFat.text.toString().toFloat()
                val carbohydrate = binding.inputCarbohydrate.text.toString().toFloat()

                // Konversi input ke bentuk yang sesuai untuk model
                val input = arrayOf(
                    floatArrayOf(protein, fat, carbohydrate) // Bentuk [1, 3]
                )

                // Prediksi menggunakan model
                val prediction = tfliteHelper.predict(input)
                val predictedValue = prediction[0][0]

                // Tampilkan hasil prediksi di TextView
                binding.textView.text = "Hasil Prediksi: %.2f".format(predictedValue)
                Log.d("TFLitePrediction", "Predicted Calories: $predictedValue")

                // Simpan data ke Firebase Firestore
                saveToFirebase(protein, fat, carbohydrate, predictedValue)

            } catch (e: Exception) {
                // Tangani jika input tidak valid
                showSnackbar("Input tidak valid")
                Log.e("InputError", "Error parsing input", e)
            }
        }

        // FAB untuk membuka Bottom Sheet dan menampilkan data
        binding.fabShowData.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_data_bottom_sheet, null)

            // Menampilkan data dari Firebase ke dalam Bottom Sheet
            loadDataFromFirebase(view)

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        return view
    }

    private fun loadDataFromFirebase(view: View) {
        val tableLayout = view.findViewById<TableLayout>(R.id.tableLayout)

        // Bersihkan TableLayout agar tidak ada baris yang menumpuk
        tableLayout.removeAllViews()

        // Tambahkan kembali header row jika diperlukan
        val headerRow = TableRow(requireContext()).apply {
            addView(createHeaderTextView("Protein (g)"))
            addView(createHeaderTextView("Fat (g)"))
            addView(createHeaderTextView("Carbohydrate (g)"))
            addView(createHeaderTextView("Predicted Calories"))
        }
        tableLayout.addView(headerRow)

        // Ambil data dari Firestore
        firestore.collection("calorie_predictions")
            .get()
            .addOnSuccessListener { result ->
                for (document: QueryDocumentSnapshot in result) {
                    val protein = document.getDouble("protein")?.toString() ?: "0"
                    val fat = document.getDouble("fat")?.toString() ?: "0"
                    val carbohydrate = document.getDouble("carbohydrate")?.toString() ?: "0"
                    val predictedCalories = document["predictedCalories"] as String

                    // Tambahkan baris data
                    val row = TableRow(requireContext())
                    row.addView(createDataTextView(protein))
                    row.addView(createDataTextView(fat))
                    row.addView(createDataTextView(carbohydrate))
                    row.addView(createDataTextView(predictedCalories))
                    tableLayout.addView(row)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseError", "Error getting documents.", e)
                showSnackbar("Gagal mengambil data dari Firebase")
            }
    }

    private fun createHeaderTextView(text: String): TextView {
        return TextView(requireContext()).apply {
            this.text = text
            textSize = 16f
            setPadding(8, 8, 8, 8)
            setTextColor(requireContext().getColor(R.color.black))
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
    }

    private fun createDataTextView(text: String): TextView {
        return TextView(requireContext()).apply {
            this.text = text
            textSize = 14f
            setPadding(8, 8, 8, 8)
            setTextColor(requireContext().getColor(R.color.black))
        }
    }

    private fun saveToFirebase(protein: Float, fat: Float, carbohydrate: Float, predictedValue: Float) {
        // Round the predicted value to 2 decimal places using String format
        val roundedPrediction = String.format("%.2f", predictedValue)

        // Data that will be saved
        val predictionData = hashMapOf(
            "protein" to protein,
            "fat" to fat,
            "carbohydrate" to carbohydrate,
            "predictedCalories" to roundedPrediction,  // Store as String
            "timestamp" to System.currentTimeMillis()
        )

        // Save data to the "calorie_predictions" collection
        firestore.collection("calorie_predictions")
            .add(predictionData)
            .addOnSuccessListener {
                Log.d("FirebaseFirestore", "Data successfully saved!")
                showSnackbar("Data successfully saved")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseFirestore", "Failed to save data", e)
                showSnackbar("Failed to save data")
            }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        tfliteHelper.close()
        _binding = null
    }
}
