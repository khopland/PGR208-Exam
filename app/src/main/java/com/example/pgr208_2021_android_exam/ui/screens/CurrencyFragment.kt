package com.example.pgr208_2021_android_exam.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.data.domain.CryptoCurrency
import com.example.pgr208_2021_android_exam.data.getImg
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCurrencyBinding.bind(view)
        mWalletViewModel.getWallet(cryptoType = cryptoCurrency.symbol)

        binding.apply {
            mWalletViewModel.walletLiveData.observe(viewLifecycleOwner, { wallet ->
                if (wallet == null) {
                    this.btnSell.isEnabled = false
                    this.currencyText.text = "you don't have this crypto"
                } else {
                    this.btnSell.isEnabled = true
                    this.currencyText.text =
                        "you have ${wallet.amount} of ${wallet.cryptoType}\n " +
                                "${wallet.amount} x ${cryptoCurrency.priceInUSD}\n " +
                                "value ${(wallet.amount * cryptoCurrency.priceInUSD)} USD"
                }
            })
            mWalletViewModel.Dollar.observe(viewLifecycleOwner, {
                this.btnBuy.isEnabled = (it != 0)
            })
            this.tvCurrencyName.text = cryptoCurrency.name
            this.tvCurrencySymbol.text = cryptoCurrency.symbol
            this.tvCurrencyRate.text = cryptoCurrency.priceInUSD.toString()
        }

        getImg(requireContext(), cryptoType = cryptoCurrency.symbol, icon = binding.ivCurrencyIcon)
    }

}