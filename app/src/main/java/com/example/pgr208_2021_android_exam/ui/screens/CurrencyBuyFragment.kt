package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pgr208_2021_android_exam.R

class CurrencyBuyFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = CurrencyBuyFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency_buy, container, false)
    }
}