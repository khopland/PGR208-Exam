package com.example.pgr208_2021_android_exam.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pgr208_2021_android_exam.data.CoinCapService
import com.example.pgr208_2021_android_exam.data.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Choosing to use AndroidViewModel here if we need to work with "getString(...)"-resources
class OverViewModel(application: Application) : AndroidViewModel(application) {

    val coinCapService: CoinCapService = CoinCapApi.coinCapService

    //Error handling...
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception>
        get() = _error

    //Fetch all
    private val _cryptoCurrencies = MutableLiveData<List<CryptoCurrency>>()
    val cryptoCurrencies: LiveData<List<CryptoCurrency>>
        get() = _cryptoCurrencies

    init {
        fetchAllCryptoCurrency()
    }

    fun fetchAllCryptoCurrency() {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val currencies = coinCapService.getAllCrypto().toDomainModel()
                // Don't update the livedata if we receive an empty-list or null
                if (currencies.isNullOrEmpty()) return@launch
                _cryptoCurrencies.postValue(currencies)
            } catch (error: Exception) {
                _error.postValue(error)
            }
        }
    }
}