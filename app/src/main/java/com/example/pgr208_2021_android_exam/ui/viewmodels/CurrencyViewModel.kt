package com.example.pgr208_2021_android_exam.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.pgr208_2021_android_exam.data.CoinCapService
import com.example.pgr208_2021_android_exam.data.domain.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyViewModel(application: Application): AndroidViewModel(application) {

    private val coinCapService: CoinCapService = CoinCapApi.coinCapService

    init {
        getAllRates()
    }

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

    // Map of rates etc.
    private val _currencyRates = MutableLiveData<Map<String, CoinRate>>()
    val currencyRates: LiveData<Map<String, CoinRate>>
        get() = _currencyRates

    private val _currencyRate = MutableLiveData<CoinRate>()
    val currencyRate: LiveData<CoinRate>
        get() = _currencyRate

     fun getAllRates() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // This is to get all rates -
                // (the rates-endpoint doesn't have them all... -
                // assets-endpoint has for all crypto...)
                val currencies = withContext(Dispatchers.IO) {
                    coinCapService.getAllCrypto().toDomainModel().toMutableList()
                }

                // We need this explicitly right here -
                // because the "fromCryptoTo..."-function below, expects it to be in the rates-map...
                val dollarRate = withContext(Dispatchers.IO) {
                    coinCapService.getRateById("united-states-dollar").toDomainModel()
                }

                val rates: MutableMap<String, CoinRate> = mutableMapOf(dollarRate.symbol to dollarRate)

                rates.putAll(fromCryptoCurrenciesToCoinRates(currencies))

                _currencyRates.postValue(rates)
            } catch (err: java.lang.Exception) {
                Log.e(this::class.java.simpleName, err.toString())
            }
        }
    }

    fun getRateBySymbol(symbol: String) {

        val rate = _currencyRates.value?.get(symbol)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val coinRate = withContext(Dispatchers.IO) { coinCapService.getRateById(rate?.name ?: "no_id") }
                _currencyRate.postValue(coinRate.toDomainModel())
            } catch (err: java.lang.Exception) {
                Log.e(this::class.java.simpleName, err.toString())
            }
        }
    }

}