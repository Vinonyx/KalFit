package com.example.projectbeta.ui.view.Login

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
import com.example.projectbeta.ui.view.MainActivity
import com.example.projectbeta.R
import com.example.projectbeta.ui.view.Register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val btnSubmit: Button = findViewById(R.id.btnSubmit)
        val btnRegis: TextView = findViewById(R.id.btnRegis)
        val email: EditText = findViewById(R.id.email)
        val password: EditText = findViewById(R.id.password)

        // Periksa apakah pengguna sudah login
        if (firebaseAuth.currentUser != null) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        btnSubmit.setOnClickListener {
            val email = email.text.toString()
            val pwd = password.text.toString()

            if (email.isNotEmpty() && pwd.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login berhasil, arahkan ke MainActivity
                            val i = Intent(this, MainActivity::class.java)
                            i.putExtra("name", "Welcome, $email")
                            startActivity(i)
                            finish()
                        } else {
                            // Login gagal
                            Toast.makeText(
                                this,
                                "Login failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        btnRegis.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
    }
}
