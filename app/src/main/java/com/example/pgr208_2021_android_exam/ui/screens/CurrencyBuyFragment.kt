package com.example.pgr208_2021_android_exam.ui.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pgr208_2021_android_exam.data.getImg
import com.example.pgr208_2021_android_exam.data.rounding
import com.example.pgr208_2021_android_exam.database.viewModel.BuyAndSellViewModel
import com.example.pgr208_2021_android_exam.databinding.CurrencyBuyBinding
import com.example.pgr208_2021_android_exam.ui.viewmodels.CurrencyViewModel
import kotlin.math.floor

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

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Extract given navigation args
        val args = CurrencyBuyFragmentArgs.fromBundle(requireArguments())

        // populate the viewModel with updated info of the selected cryptoCurrency
        viewModel.fetchCryptoCurrencyById(args.cryptoType)

        // Fill in the "selected currency info header" with values...
        viewModel.selectedCryptoCurrency.observe(viewLifecycleOwner, { cryptoCurrency ->
            binding.apply {
                getImg(
                    context = requireContext(),
                    cryptoType = cryptoCurrency.symbol,
                    icon = binding.ivCurrencyIcon
                )
                // Currency info header
                tvCurrencyName.text = cryptoCurrency.name
                tvCurrencySymbol.text = cryptoCurrency.symbol
                val currencyRate = "$${rounding(cryptoCurrency.priceInUSD)}"
                tvCurrencyRate.text = currencyRate
                // Currency buy parts
                tvCryptoCurrencySymbol.text = cryptoCurrency.symbol


            }
        })

        // Prevent the user from buying before filling out a dollar-value
        binding.btnBuy.isEnabled = false

        // Watch for typed in USD-amount the user wants to spend
        binding.tvDollarValue.addTextChangedListener(textWatcher)

        // Give needed details for a transaction to buyAndSellViewModel
        binding.btnBuy.setOnClickListener {
            binding.btnBuy.isEnabled = false
            val cR =
                viewModel.currencyRates.value?.get(binding.tvCryptoCurrencySymbol.text.toString())?.rateUSD
            if (cR != null) {
                buyAndSellViewModel.buy(
                    isSelling = false,
                    dollar = floor(binding.tvDollarValue.text.toString().toDouble()).toLong(),
                    conversionRate = cR,
                    cryptoType = binding.tvCryptoCurrencySymbol.text.toString()
                )
            } else {
                val text =
                    "your transaction did not go through,\nsomething went wrong with the conversion rate"
                val duration = Toast.LENGTH_LONG
                val toast = Toast.makeText(requireContext(), text, duration)
                toast.show()
                binding.btnBuy.isEnabled = true
            }
        }

        buyAndSellViewModel.successLiveData.observe(viewLifecycleOwner, { status ->
            status?.let {
                if (status) {
                    buyAndSellViewModel.successLiveData.value = null
                    val text = "you bought ${binding.tvCurrencyName.text}"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(requireContext(), text, duration)
                    toast.show()


                    // Navigating back to CurrencyFragment after successful buy
                    viewModel.selectedCryptoCurrency.observe(viewLifecycleOwner, {
                        findNavController().navigate(
                            CurrencyBuyFragmentDirections.actionCurrencyBuyFragmentToCurrencyFragment(
                                it
                            )
                        )
                    })

                    binding.btnBuy.isEnabled = true
                } else if (!status) {
                    buyAndSellViewModel.successLiveData.value = null
                    val text = "your transaction did not go through"
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(requireContext(), text, duration)
                    toast.show()
                    binding.btnBuy.isEnabled = true
                }
            }
        })
    }

    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(field: Editable) {

            // NB: ...substring(1) to get the values after currency sign ($)
            val currencyRate = binding.tvCurrencyRate.text.toString().substring(1).toDouble()

            // Display "0" after the user removes all text in dollar-input field
            val inputDollarValue =
                if (field.isBlank() || field[0] == '.') 0 else binding.tvDollarValue.text.toString().toInt()

            // rounding to display value with 1-2 decimals
            binding.tvCalculatedCryptoValue.text = "${rounding(inputDollarValue / currencyRate)}"

            binding.btnBuy.isEnabled = field.isNotBlank() && field.isNotEmpty()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllRates()
        viewModel.currencyRates.observe(viewLifecycleOwner, {
            val cr = it[binding.tvCryptoCurrencySymbol.text.toString()]?.rateUSD
            if (cr != null)
                binding.tvCurrencyRate.text = "$${rounding(cr)}"
        })
    }
}