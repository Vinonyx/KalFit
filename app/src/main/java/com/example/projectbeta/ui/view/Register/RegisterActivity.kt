package com.example.projectbeta.ui.view.Register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projectbeta.ui.view.Login.LoginActivity
import com.example.projectbeta.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi Firebase Authentication dan Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)
        val cpassword: EditText = findViewById(R.id.cpassword)
        val btnLogin: TextView = findViewById(R.id.btnLogin)
        val btnSubmit: Button = findViewById(R.id.btnSubmit)

        btnLogin.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        btnSubmit.setOnClickListener {
            val usn = email.text.toString().trim()
            val pwd = password.text.toString().trim()
            val cpwd = cpassword.text.toString().trim()

            if (usn.isEmpty() || pwd.isEmpty() || cpwd.isEmpty()) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (pwd != cpwd) {
                Toast.makeText(this, "Konfirmasi password tidak sesuai", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mendaftarkan user ke Firebase Authentication
            firebaseAuth.createUserWithEmailAndPassword(usn, pwd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = task.result?.user?.uid ?: return@addOnCompleteListener
                        val user = hashMapOf(
                            "userId" to userId,
                            "username" to usn
                        )

                        // Simpan data user ke Firestore
                        firestore.collection("users")
                            .document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT)
                                    .show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Gagal menyimpan data: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "Registrasi gagal: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
