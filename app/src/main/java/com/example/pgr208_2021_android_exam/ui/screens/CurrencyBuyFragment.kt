package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
        buyAndSellViewModel.successLiveData.observe(viewLifecycleOwner, { status ->
            status?.let {
                if (status) {
                    buyAndSellViewModel.successLiveData.value = null
                    val text = "you bough crypto"
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(requireContext(), text, duration)
                    toast.show()
                } else if (!status) {
                    buyAndSellViewModel.successLiveData.value = null
                    val text = "your transaction did not go threw"
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(requireContext(), text, duration)
                    toast.show()
                }
            }
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currency_buy, container, false)
    }
}