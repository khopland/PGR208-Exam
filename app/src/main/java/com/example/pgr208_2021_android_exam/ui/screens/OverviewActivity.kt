package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.pgr208_2021_android_exam.databinding.ActivityOverviewBinding

class OverviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creating the static layout class and inflating the content into the contentView -
        // (middle-part between top and bottom navbar etc.)
        binding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}