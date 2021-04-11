package com.example.pgr208_2021_android_exam.ui.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.databinding.ActivityOverviewBinding

class OverviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityOverviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPortFolio.setOnClickListener {

            // TODO: Remove this and remove the visibility-attribute in the XML-file after setting up RecyclerView...
            // Make the fragmentContainer visible, and hide the RecyclerView in the XML-file...
            binding.fragmentContainer.visibility = View.VISIBLE
            binding.currencyList.visibility = View.GONE

            supportFragmentManager.apply {
                beginTransaction()
                    .replace(binding.fragmentContainer.id, PortfolioFragment())
                    .commit()
            }
        }
    }
}