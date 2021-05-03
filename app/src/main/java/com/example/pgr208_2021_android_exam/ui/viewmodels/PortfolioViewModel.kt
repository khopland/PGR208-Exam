package com.example.pgr208_2021_android_exam.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.pgr208_2021_android_exam.data.CoinCapService
import com.example.pgr208_2021_android_exam.data.domain.CoinCapApi
import com.example.pgr208_2021_android_exam.data.domain.CoinRate
import com.example.pgr208_2021_android_exam.data.domain.toDomainModel
import com.example.pgr208_2021_android_exam.data.rounding
import com.example.pgr208_2021_android_exam.database.entities.Wallet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    val coinCapService: CoinCapService = CoinCapApi.coinCapService

    // Fetch list of rates for all currencies...
    private val _currencyRates = MutableLiveData<List<CoinRate>>()
    val currencyRates: LiveData<List<CoinRate>>
        get() = _currencyRates

    init {
        getAllRates()
    }

    private fun getAllRates() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val rates = coinCapService.getAllRates().toDomainModel()
                if (rates.isNullOrEmpty()) return@launch
                _currencyRates.postValue(rates)
            } catch (err: Exception) {
                Log.e(this@PortfolioViewModel::class.java.simpleName, err.toString())
            }
        }
    }

    /* UI related functionality */
    fun transformIntoOwnedWallets(wallets: List<Wallet>): List<OwnedWallet> {

        // This is placed here in case someone uses this method without observing currencyRates -
        // Then they will get an empty-list before even trying to do any transformations (work).
        currencyRates.value ?: emptyList()

        // Create a filtered list of only ownedCurrencies
        val ownedWallets: MutableList<OwnedWallet> = mutableListOf()

        // Go through all wallets, extract their USD-rate and create a ownedWallet for each
        wallets.forEach { wallet ->
           // Get the corresponding CoinRate for the provided wallet
            // We need the currencyRate-value from the CoinRate later
            val ownedCurrency = currencyRates.value?.let { rates ->
                rates.find { coinRate -> coinRate.symbol == wallet.cryptoType }
            }

            // If the coinRate was found -
            // then prepare a "ownedWallet"-for it and add it to the list
            ownedCurrency?.let {
                val currencyAmount = rounding(wallet.amount)
                val currencyRate = rounding(ownedCurrency.rateUSD)
                val totalInUSD = rounding(currencyAmount * currencyRate)

                val ownedWallet = OwnedWallet(
                        cryptoType = wallet.cryptoType,
                        amount = currencyAmount,
                        currencyRate = currencyRate,
                        totalInUSD = totalInUSD
                )

                ownedWallets.add(ownedWallet)
            }
        }

        return ownedWallets
    }
}

// Data class for presentation of ownedWallets
data class OwnedWallet(
        val cryptoType: String,
        val amount: Double,
        val currencyRate: Double,
        val totalInUSD: Double
)