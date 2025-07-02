package com.example.projectbeta.utils

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.IOException

class TFLiteHelper(private val context: Context, private val modelPath: String) {

    private var interpreter: Interpreter? = null

    init {
        try {
            interpreter = Interpreter(loadModelFile(context, modelPath))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Fungsi untuk memuat model TFLite dari file
    @Throws(IOException::class)
    private fun loadModelFile(context: Context, modelPath: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // Fungsi untuk melakukan inferensi
    fun predict(input: Array<FloatArray>): Array<FloatArray> {
        val output = Array(1) { FloatArray(1) } // Sesuaikan dengan jumlah output
        interpreter?.run(input, output)
        return output
    }

    // Fungsi untuk membersihkan interpreter
    fun close() {
        interpreter?.close()
    }
}