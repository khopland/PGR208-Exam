package com.example.pgr208_2021_android_exam.ui.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.databinding.ActivityOverviewBinding

class OverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityOverviewBinding.inflate(layoutInflater);
        setContentView(binding.root)

        binding.btnPortFolio.setOnClickListener {
            supportFragmentManager.apply {
                beginTransaction()
                    .replace(binding.fragmentContainer.id, PortfolioFragment())
                    .commit()
            }
        }
    }
}