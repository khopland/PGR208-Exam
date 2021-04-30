package com.example.pgr208_2021_android_exam.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.pgr208_2021_android_exam.data.CoinCapService
import com.example.pgr208_2021_android_exam.data.domain.CoinCapApi
import com.example.pgr208_2021_android_exam.data.domain.CryptoCurrency
import com.example.pgr208_2021_android_exam.data.domain.toDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrencyViewModel(application: Application): AndroidViewModel(application) {

    val coinCapService: CoinCapService = CoinCapApi.coinCapService

    //Error handling...
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception>
        get() = _error

    //Fetch selected/clicked cryptoCurrency
    private val _selectedCryptoCurrency = MutableLiveData<CryptoCurrency>()
    val selectedCryptoCurrency: LiveData<CryptoCurrency>
        get() = _selectedCryptoCurrency

    fun fetchCryptoCurrencyById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cryptoCurrency = coinCapService.getCryptoById(id)
                _selectedCryptoCurrency.postValue(cryptoCurrency.toDomainModel())
            } catch (error: Exception) {
                _error.postValue(error)
            }
        }
    }

    fun setSelectedCurrency(currency: CryptoCurrency) {
        //_selectedCryptoCurrency.postValue(currency)
        fetchCryptoCurrencyById(currency.type)
    }

}