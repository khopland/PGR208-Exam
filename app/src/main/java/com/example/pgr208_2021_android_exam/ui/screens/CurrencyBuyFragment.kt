package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.database.viewModel.BuyAndSellViewModel

class CurrencyBuyFragment : Fragment() {

    private lateinit var buyAndSellViewModel: BuyAndSellViewModel

    companion object {
        @JvmStatic
        fun newInstance() = CurrencyBuyFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buyAndSellViewModel = ViewModelProvider(this).get(BuyAndSellViewModel::class.java)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency_buy, container, false)
    }
}