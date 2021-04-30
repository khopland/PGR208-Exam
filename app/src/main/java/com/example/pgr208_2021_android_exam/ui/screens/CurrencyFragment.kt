package com.example.pgr208_2021_android_exam.ui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pgr208_2021_android_exam.data.getImg
import com.example.pgr208_2021_android_exam.database.viewModel.WalletViewModel
import com.example.pgr208_2021_android_exam.databinding.FragmentCurrencyBinding
import com.example.pgr208_2021_android_exam.ui.viewmodels.CurrencyViewModel

class CurrencyFragment : Fragment() {
    private lateinit var binding: FragmentCurrencyBinding
    private lateinit var mWalletViewModel: WalletViewModel
    private lateinit var viewModel: CurrencyViewModel

    companion object {
        @JvmStatic
        fun newInstance() = CurrencyFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?) : View {

        binding = FragmentCurrencyBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)
        mWalletViewModel = ViewModelProvider(this).get(WalletViewModel::class.java)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Extract arguments from navigation-Directions, passed from OverviewFragment
        val args = CurrencyFragmentArgs.fromBundle(requireArguments())

        val cryptoCurrency = args.cryptoCurrency

        viewModel.setSelectedCurrency(currency = cryptoCurrency)

        mWalletViewModel.getWallet(cryptoType = cryptoCurrency.symbol)

        mWalletViewModel.walletLiveData.observe(viewLifecycleOwner, { wallet ->
            if (wallet == null) {
                binding.btnSell.isEnabled = false
                binding.currencyText.text = "you don't have this crypto"
            } else {
                binding.btnSell.isEnabled = true
                binding.currencyText.text =
                    "you have ${wallet.amount} of ${wallet.cryptoType}\n " +
                            "${wallet.amount} x ${cryptoCurrency.priceInUSD}\n " +
                            "value ${(wallet.amount * cryptoCurrency.priceInUSD)} USD"
            }
        })
        mWalletViewModel.Dollar.observe(viewLifecycleOwner, {
            binding.btnBuy.isEnabled = (it != 0)
        })

        binding.apply {
            this.tvCurrencyName.text = cryptoCurrency.name
            this.tvCurrencySymbol.text = cryptoCurrency.symbol
            this.tvCurrencyRate.text = cryptoCurrency.priceInUSD.toString()
        }

        getImg(requireContext(), cryptoType = cryptoCurrency.symbol, icon = binding.ivCurrencyIcon)

        binding.btnBuy.setOnClickListener {
            findNavController().navigate(CurrencyFragmentDirections.actionCurrencyFragmentToCurrencyBuyFragment(cryptoCurrency.type))
        }

        binding.btnSell.setOnClickListener {
            findNavController().navigate(CurrencyFragmentDirections.actionCurrencyFragmentToCurrencySellFragment(cryptoCurrency.type))
        }
    }
}