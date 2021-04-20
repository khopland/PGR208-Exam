package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.database.viewModel.AllWalletsViewModel

class PortfolioFragment : Fragment(R.layout.fragment_portfolio) {
    private lateinit var allWalletsViewModel: AllWalletsViewModel

    companion object {
        @JvmStatic
        fun newInstance() = PortfolioFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allWalletsViewModel = ViewModelProvider(this).get(AllWalletsViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portfolio, container, false)
    }
}
