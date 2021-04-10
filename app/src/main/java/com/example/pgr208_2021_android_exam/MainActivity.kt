package com.example.pgr208_2021_android_exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme (R.style.splashScreenTheme)
        setContentView(R.layout.activity_main)
    }
}