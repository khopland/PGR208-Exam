package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.pgr208_2021_android_exam.data.domain.getImg
import com.example.pgr208_2021_android_exam.databinding.CurrencySellBinding
import com.example.pgr208_2021_android_exam.ui.viewmodels.CurrencyViewModel

class CurrencySellFragment : Fragment() {
    private lateinit var binding: CurrencySellBinding
    private lateinit var viewModel: CurrencyViewModel

    companion object {
        @JvmStatic
        fun newInstance() = CurrencySellFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = CurrencySellBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CurrencySellFragmentArgs.fromBundle(requireArguments())
        viewModel.fetchCryptoCurrencyById(args.cryptoType)

        viewModel.selectedCryptoCurrency.observe(viewLifecycleOwner, { cryptoCurrency ->
            binding.apply {
                getImg(context = requireContext(), cryptoType = cryptoCurrency.symbol, icon = binding.ivCurrencyIcon)
                // Currency info header
                tvCurrencyName.text = cryptoCurrency.name
                tvCurrencySymbol.text = cryptoCurrency.symbol
                tvCurrencyRate.text = cryptoCurrency.priceInUSD.toString()
                // Currency sell parts
                tvCryptoCurrencySymbol.text = cryptoCurrency.symbol
            }
        })

        binding.tvCryptoValue.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(field: Editable?) {
            if (field.isNullOrBlank()) return

            val currencyRate = binding.tvCurrencyRate.text.toString().toDouble()
            val inputCryptoValue = binding.tvCryptoValue.text.toString().toDouble()

            binding.tvDollarValue.text = "${inputCryptoValue * currencyRate}"
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}