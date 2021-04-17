package com.example.pgr208_2021_android_exam

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.database.viewModel.StartViewModel
import com.example.pgr208_2021_android_exam.database.viewModel.WalletViewModel
import com.example.pgr208_2021_android_exam.databinding.ActivityMainBinding
import com.example.pgr208_2021_android_exam.ui.screens.OverviewActivity
import com.example.pgr208_2021_android_exam.ui.screens.PortfolioFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var startViewModel: StartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.splashScreenTheme)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //startActivity(Intent(this@MainActivity, OverviewActivity::class.java))
        startViewModel = ViewModelProvider(this).get(StartViewModel::class.java)
        // We took inspiration for this setup from the following video: https://www.youtube.com/watch?v=bRusWAEn5GA
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            startActivity(Intent(this@MainActivity, OverviewActivity::class.java))
            finish()
        }, 4000)
    }
}