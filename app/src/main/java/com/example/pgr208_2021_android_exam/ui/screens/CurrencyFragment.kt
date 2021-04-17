package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.data.domain.CryptoCurrency
import com.example.pgr208_2021_android_exam.data.domain.getImg
import com.example.pgr208_2021_android_exam.database.viewModel.WalletViewModel
import com.example.pgr208_2021_android_exam.databinding.FragmentCurrencyBinding

class CurrencyFragment(private val cryptoCurrency: CryptoCurrency) :
    Fragment(R.layout.fragment_currency) {
    private lateinit var binding: FragmentCurrencyBinding
    private lateinit var mWalletViewModel: WalletViewModel

    companion object {
        @JvmStatic
        fun newInstance(currency: CryptoCurrency) = CurrencyFragment(currency)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Log.d(this::class.java.simpleName, cryptoCurrency.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mWalletViewModel = ViewModelProvider(this).get(WalletViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCurrencyBinding.bind(view)
        mWalletViewModel.getWallet(cryptoType = cryptoCurrency.symbol)

        binding.apply {
            mWalletViewModel.walletLiveData.observe(viewLifecycleOwner, Observer { wallet ->
                if (wallet == null) {
                    this.currencyText.text = "you don't have this crypto"
                } else {
                    this.currencyText.text =
                        "you have ${wallet.amount} of ${wallet.cryptoType}\n " +
                                "${wallet.amount} x ${cryptoCurrency.priceInUSD}\n " +
                                "value ${(wallet.amount * cryptoCurrency.priceInUSD)} USD"
                }
            })

            this.tvCurrencyName.text = cryptoCurrency.name
            this.tvCurrencySymbol.text = cryptoCurrency.symbol
            this.tvCurrencyRate.text = cryptoCurrency.priceInUSD.toString()
        }

        getImg(requireContext(), cryptoType = cryptoCurrency.symbol, icon = binding.ivCurrencyIcon)
    }

}