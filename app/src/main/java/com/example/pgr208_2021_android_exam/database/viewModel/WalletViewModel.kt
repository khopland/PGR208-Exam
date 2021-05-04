package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.TransactionRepository
import com.example.pgr208_2021_android_exam.database.entities.Wallet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository

    private val _walletLiveData: MutableLiveData<Wallet?> = MutableLiveData(null)
    val walletLiveData: LiveData<Wallet?> = _walletLiveData

    private val _dollar: MutableLiveData<Number> = MutableLiveData()
    val dollar: LiveData<Number> = _dollar

    init {
        val transactionDao = DataBase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        getDollar()
    }

    // Function to get the current USD, you can spend on buying cryptoCurrency
    private fun getDollar() {
        viewModelScope.launch(Dispatchers.IO) {
            _dollar.postValue(repository.getDollar())
        }
    }

    //function to get wallet for the provided cryptoType
    fun getWallet(cryptoType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _walletLiveData.postValue(repository.getWallet(cryptoType))
        }
    }
}