package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.data.domain.getImg
import com.example.pgr208_2021_android_exam.database.viewModel.BuyAndSellViewModel
import com.example.pgr208_2021_android_exam.databinding.CurrencyBuyBinding
import com.example.pgr208_2021_android_exam.ui.viewmodels.CurrencyViewModel

class CurrencyBuyFragment : Fragment() {
    private lateinit var binding: CurrencyBuyBinding
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var buyAndSellViewModel: BuyAndSellViewModel

    companion object {
        @JvmStatic
        fun newInstance() = CurrencyBuyFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrencyBuyBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CurrencyBuyFragmentArgs.fromBundle(requireArguments())
        viewModel.fetchCryptoCurrencyById(args.cryptoType)

        viewModel.selectedCryptoCurrency.observe(viewLifecycleOwner, { cryptoCurrency ->
            binding.apply {
                getImg(context = requireContext(), cryptoType = cryptoCurrency.symbol, icon = binding.ivCurrencyIcon)
                // Currency info header
                tvCurrencyName.text = cryptoCurrency.name
                tvCurrencySymbol.text = cryptoCurrency.symbol
                tvCurrencyRate.text = cryptoCurrency.priceInUSD.toString()
                // Currency buy parts
                tvCryptoCurrencySymbol.text = cryptoCurrency.symbol
            }
        })

        binding.tvDollarValue.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(field: Editable?) {
            if (field.isNullOrBlank()) return

            val currencyRate = binding.tvCurrencyRate.text.toString().toDouble()
            val inputDollarValue = binding.tvDollarValue.text.toString().toInt()

            binding.tvCalculatedCryptoValue.text = "${inputDollarValue / currencyRate}"
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}