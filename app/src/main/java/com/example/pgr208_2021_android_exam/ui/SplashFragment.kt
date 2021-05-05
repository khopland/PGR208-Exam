package com.example.pgr208_2021_android_exam.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pgr208_2021_android_exam.databinding.FragmentSplashBinding
import com.example.pgr208_2021_android_exam.ui.screens.OverviewActivity

/**
 * Fragment that will be the "splash-screen" that MainActivity loads
 * Had to do this to make navigation work as expected...
 */
class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSplashBinding.inflate(layoutInflater)

        // We took inspiration for this setup from the following video: https://www.youtube.com/watch?v=bRusWAEn5GA
        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            startActivity(Intent(requireContext(), OverviewActivity::class.java))
            // Get the activity that initialized this fragment (MainActivity) and finish it...
            requireActivity().finish()
        }, 4000)

        return binding.root
    }
}