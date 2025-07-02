package com.example.projectbeta.ui.view.WelcomeScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.projectbeta.ui.adapter.PageAdapter
import com.example.projectbeta.ui.view.Login.LoginActivity
import com.example.projectbeta.R
import com.example.projectbeta.ui.view.WelcomeScreen.FirstWelcomeFragment
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class WelcomeActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsIndicator: DotsIndicator
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = findViewById(R.id.viewPager)
//        dotsIndicator = findViewById(R.id.dotsIndicator)
        btnNext = findViewById(R.id.btnNext)

        // Hanya menggunakan FirstWelcomeFragment
        val fragmentList = listOf(FirstWelcomeFragment())
        val adapter = PageAdapter(this, fragmentList)

        viewPager.adapter = adapter
//        dotsIndicator.attachTo(viewPager)

        btnNext.setOnClickListener {
            finishWelcomeScreen()
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Hanya ada satu halaman, jadi tombol "Finish" langsung aktif
                btnNext.text = "Next"
            }
        })
    }

    private fun finishWelcomeScreen() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
