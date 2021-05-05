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
import com.example.pgr208_2021_android_exam.data.getImg
import com.example.pgr208_2021_android_exam.data.rounding
import com.example.pgr208_2021_android_exam.database.viewModel.BuyAndSellViewModel
import com.example.pgr208_2021_android_exam.database.viewModel.WalletViewModel
import com.example.pgr208_2021_android_exam.databinding.CurrencySellBinding
import com.example.pgr208_2021_android_exam.ui.viewmodels.CurrencyViewModel
import kotlin.math.round

class CurrencySellFragment : Fragment() {
    private lateinit var binding: CurrencySellBinding
    private lateinit var viewModel: CurrencyViewModel
    private lateinit var buyAndSellViewModel: BuyAndSellViewModel
    private lateinit var mWalletViewModel: WalletViewModel


    companion object {
        @JvmStatic
        fun newInstance() = CurrencySellFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = CurrencySellBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(CurrencyViewModel::class.java)
        buyAndSellViewModel = ViewModelProvider(this).get(BuyAndSellViewModel::class.java)
        mWalletViewModel = ViewModelProvider(this).get(WalletViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = CurrencySellFragmentArgs.fromBundle(requireArguments())
        viewModel.fetchCryptoCurrencyById(args.cryptoType)

        viewModel.selectedCryptoCurrency.observe(viewLifecycleOwner, { cryptoCurrency ->
            //TODO: not optimal observe in a observe
            mWalletViewModel.getWallet(cryptoType = cryptoCurrency.symbol)
            mWalletViewModel.walletLiveData.observe(viewLifecycleOwner, { wallet ->
                binding.apply {
                    if (wallet != null)
                        tvYouHave.text = "You have ${wallet.amount}"
                    else
                        tvYouHave.text = ""
                }
            })

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
                // Currency sell parts
                tvCryptoCurrencySymbol.text = cryptoCurrency.symbol
            }
        })

        // Prevent the user from buying before filling out a crypto-value
        binding.btnSell.isEnabled = false

        binding.tvCryptoValue.addTextChangedListener(textWatcher)

        binding.btnSell.setOnClickListener {
            buyAndSellViewModel.sell(
                isSelling = true,
                dollar = round(binding.tvDollarValue.text.toString().toDouble()).toLong(),
                conversionRate = binding.tvCurrencyRate.text.toString().substring(1).toDouble(),
                cryptoType = binding.tvCryptoCurrencySymbol.text.toString()
            )
        }

        buyAndSellViewModel.successLiveData.observe(viewLifecycleOwner, { status ->
            status?.let {
                if (status) {
                    buyAndSellViewModel.successLiveData.value = null
                    val text = "you sold ${binding.tvCurrencyName.text}"
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(requireContext(), text, duration)
                    toast.show()
                    mWalletViewModel.getWallet(cryptoType = binding.tvCryptoCurrencySymbol.text.toString())
                } else if (!status) {
                    buyAndSellViewModel.successLiveData.value = null
                    val text = """your transaction did not go through
                        |you have "${mWalletViewModel.walletLiveData.value?.amount}"""".trimMargin()
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(requireContext(), text, duration)
                    toast.show()
                }
            }
        })
    }

    private val textWatcher = object : TextWatcher {

        override fun afterTextChanged(field: Editable) {

            val currencyRate = binding.tvCurrencyRate.text.toString().substring(1).toDouble()

            val inputCryptoValue =
                if (field.isBlank()) 0.0 else binding.tvCryptoValue.text.toString().toDouble()

            val calculatedUSD = rounding(inputCryptoValue * currencyRate)

            binding.tvDollarValue.text = "${rounding(inputCryptoValue * currencyRate)}"

            binding.btnSell.isEnabled =
                (field.isNotBlank() && field.isNotEmpty()) && calculatedUSD >= 1
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
}