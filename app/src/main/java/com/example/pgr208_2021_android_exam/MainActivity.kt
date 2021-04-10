package com.example.pgr208_2021_android_exam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.core.content.ContextCompat.startActivity
import com.example.pgr208_2021_android_exam.databinding.ActivityMainBinding
import com.example.pgr208_2021_android_exam.ui.screens.OverviewActivity
import com.example.pgr208_2021_android_exam.ui.screens.PortfolioFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.splashScreenTheme)

        val binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
    }

}