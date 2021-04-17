package com.example.pgr208_2021_android_exam.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pgr208_2021_android_exam.data.CoinCapService
import com.example.pgr208_2021_android_exam.data.domain.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Choosing to use AndroidViewModel here if we need to work with "getString(...)"-resources
class OverViewModel(application: Application) : AndroidViewModel(application) {

    val coinCapService: CoinCapService = CoinCapApi.coinCapService

    //Fetch all
    private val _cryptoCurrencies = MutableLiveData<List<CryptoCurrency>>()
    val cryptoCurrencies: LiveData<List<CryptoCurrency>>
        get() =_cryptoCurrencies

    init {
        fetchAllCryptoCurrency()
    }

    // TODO: Prepare fetching list of cryptoCurrencies
     fun fetchAllCryptoCurrency() {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val currencies = coinCapService.getAllCrypto().toDomainModel()
                //println(currencies)
                // Don't update the livedata if we receive an empty-list or null
                if (currencies.isNullOrEmpty()) return@launch
                _cryptoCurrencies.postValue(currencies)
            } catch (error: Exception) {
                _error.postValue(error)
                Log.d("fetchAll", coinCapService.getAllCrypto().toString() )
            }
        }
    }



    //Fetch selected/clicked cryptoCurrency
    private val _selectedCryptoCurrency = MutableLiveData<CryptoCurrency>()
    val selectedCryptoCurrency: LiveData<CryptoCurrency>
        get() = _selectedCryptoCurrency


    //Error handling...
    private val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception>
        get() = _error


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
        _selectedCryptoCurrency.postValue(currency)
        //fetchCryptoCurrencyById(currency.type)
    }

}