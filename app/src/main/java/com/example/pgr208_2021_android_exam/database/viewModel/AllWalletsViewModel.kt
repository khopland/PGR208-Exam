package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pgr208_2021_android_exam.data.CoinCapService
import com.example.pgr208_2021_android_exam.data.domain.CoinCapApi
import com.example.pgr208_2021_android_exam.data.domain.CoinRate
import com.example.pgr208_2021_android_exam.data.rounding
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.TransactionRepository
import com.example.pgr208_2021_android_exam.database.entities.Wallet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllWalletsViewModel(application: Application, private val rates: Map<String, CoinRate>) :
    AndroidViewModel(application) {
    private val repository: TransactionRepository
    private val coinCapService: CoinCapService

    init {
        val transactionDao = DataBase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        coinCapService = CoinCapApi.coinCapService
        getOwnedWallets()
    }

    private val _ownedWallets = MutableLiveData<Map<String, OwnedWallet>>()
    val ownedWallets: LiveData<Map<String, OwnedWallet>>
        get() = _ownedWallets

    fun getOwnedWallet(cryptoType: String) {
        _ownedWallets.value?.getValue(cryptoType)
    }

    private fun getOwnedWallets() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                // Block until wallets are fetched...
                val wallets = withContext(Dispatchers.IO) { repository.getAllWallets() }

                // "associateByTo" needs a map to "associate the key and value to"...
                val walletsMap = mutableMapOf<String, OwnedWallet>()

                // Transforming list of wallets into map, with cyrptoType as key, and wallet as value
                // example: { key: DOGE, value: { cryptoType: DOGE, amount: 2.56 } }
                val ownedWallets = wallets?.let { owned ->
                    owned.associateByTo(
                        walletsMap,
                        { it.cryptoType },
                        { transformIntoOwnedWallet(it) })
                }

                // Store empty map if we have no wallets/no wallets received from repository
                _ownedWallets.postValue(ownedWallets ?: walletsMap)
            } catch (err: Exception) {
                Log.e(this::class.java.simpleName, err.toString())
            }
        }
    }

    fun transformIntoOwnedWallet(wallet: Wallet): OwnedWallet {
        val currencyRate = rates.getValue(wallet.cryptoType)

        // Prepare values, round to 2 decimals
        val amount = rounding(wallet.amount)
        val rate = rounding(currencyRate.rateUSD)
        val totalInUSD = rounding(amount * rate)

        return OwnedWallet(
            cryptoType = wallet.cryptoType,
            amount = amount,
            currencyRate = rate,
            totalInUSD = totalInUSD
        )
    }

}

// Data class for presentation of ownedWallets
data class OwnedWallet(
    val cryptoType: String,
    val amount: Double,
    val currencyRate: Double,
    val totalInUSD: Double
)