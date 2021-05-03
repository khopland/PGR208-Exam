package com.example.pgr208_2021_android_exam

import android.content.Context
import android.content.Intent
import android.os.*
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.database.viewModel.StartViewModel
import com.example.pgr208_2021_android_exam.databinding.ActivityMainBinding
import com.example.pgr208_2021_android_exam.ui.screens.OverviewActivity

class MainActivity : AppCompatActivity() {
    private lateinit var startViewModel: StartViewModel


    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        startViewModel = ViewModelProvider(this).get(StartViewModel::class.java)

        startViewModel.successLiveData.observe(this, { status ->
            status?.let {
                if (status) {
                    startViewModel.successLiveData.value = null
                    val text = "Congrats you have gotten your starter Pack!!!"
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(application, text, duration)
                    toast.show()
                }
            }
        })

        return super.onCreateView(name, context, attrs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.splashScreenTheme)
        setContentView(R.layout.activity_main)
    }
}