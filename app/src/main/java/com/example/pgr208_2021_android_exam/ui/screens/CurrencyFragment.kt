package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.data.domain.CryptoCurrency
import com.example.pgr208_2021_android_exam.data.domain.getImg
import com.example.pgr208_2021_android_exam.databinding.FragmentCurrencyBinding

class CurrencyFragment(private val cryptoCurrency: CryptoCurrency) : Fragment(R.layout.fragment_currency) {
    private lateinit var binding: FragmentCurrencyBinding

    companion object {
        @JvmStatic
        fun newInstance(currency: CryptoCurrency) = CurrencyFragment(currency)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d(this::class.java.simpleName, cryptoCurrency.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCurrencyBinding.bind(view)

        binding.apply {
            this.tvCurrencyName.text = cryptoCurrency.name
            this.tvCurrencySymbol.text = cryptoCurrency.symbol
            this.tvCurrencyRate.text = cryptoCurrency.priceInUSD.toString()
        }

        getImg(requireContext(), cryptoType = cryptoCurrency.symbol, icon = binding.ivCurrencyIcon)
    }

}