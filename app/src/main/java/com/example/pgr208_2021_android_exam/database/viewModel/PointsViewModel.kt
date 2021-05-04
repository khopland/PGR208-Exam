package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.pgr208_2021_android_exam.data.CoinCapService
import com.example.pgr208_2021_android_exam.data.domain.CoinCapApi
import com.example.pgr208_2021_android_exam.data.domain.fromCryptoCurrenciesToCoinRates
import com.example.pgr208_2021_android_exam.data.domain.toDomainModel
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.TransactionRepository
import com.example.pgr208_2021_android_exam.ui.viewmodels.OwnedWalletsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PointsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    private val coinCapService: CoinCapService = CoinCapApi.coinCapService

    private val _pointsLiveData: MutableLiveData<Double?> = MutableLiveData()
    val pointsLiveData: LiveData<Double?> = _pointsLiveData

    // Transforming current points into formatted "user points"
    // example: 10000.0 => "Points: 10000.00 USD"
    val userPoints: LiveData<String> = pointsLiveData.map { points ->

        // initially points is: 10000.0
        val res = "${points.toString()}00"
        val valueWithDecimals = res.substring(0, res.indexOf('.') + 3)

        // Return formatted text with prepared value
        "Points: $valueWithDecimals USD"
    }

    init {
        val transactionDao = DataBase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        getPoints()
    }

    private fun getPoints() {
        viewModelScope.launch(Dispatchers.IO) {
            val api = coinCapService.getAllCrypto().toDomainModel()
            var sum = 0.0
            repository.getAllWallets()?.forEach { wallet ->
                if (wallet.cryptoType != "USD") {
                    val crypto = api.find { cryptoCurrency ->
                        cryptoCurrency.symbol == wallet.cryptoType
                    }
                    if (crypto != null)
                        sum += wallet.amount * crypto.priceInUSD
                    else
                        Log.e("cant find Crypto", crypto.toString())
                } else
                    sum += wallet.amount
            }
            _pointsLiveData.postValue(sum)
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            var sum = 0.0
            // need rates for ownedWallets...
            val rates = fromCryptoCurrenciesToCoinRates(coinCapService.getAllCrypto().toDomainModel()).toMutableMap()
            // Add missing USD-wallet...
            rates["USD"] = coinCapService.getRateById("united-states-dollar").toDomainModel()

            val ownedWalletsVM = OwnedWalletsViewModel(this@PointsViewModel.getApplication(), rates)

            repository.getAllWallets()?.forEach { wallet ->
                val ownedWallet = ownedWalletsVM.transformIntoOwnedWallet(wallet)
                sum += ownedWallet.totalInUSD
            }

            _pointsLiveData.postValue(sum)
        }
    }

}