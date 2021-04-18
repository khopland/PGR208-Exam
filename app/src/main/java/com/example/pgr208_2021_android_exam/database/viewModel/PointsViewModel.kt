package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pgr208_2021_android_exam.data.CoinCapService
import com.example.pgr208_2021_android_exam.data.domain.CoinCapApi
import com.example.pgr208_2021_android_exam.data.domain.toDomainModel
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PointsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    private val coinCapService: CoinCapService = CoinCapApi.coinCapService

    private val _pointsLiveData: MutableLiveData<Double?> = MutableLiveData()
    val pointsLiveData: LiveData<Double?> = _pointsLiveData

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


}