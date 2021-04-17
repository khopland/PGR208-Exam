package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.TransactionRepository
import com.example.pgr208_2021_android_exam.database.entities.Wallet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository

    private val _walletLiveData: MutableLiveData<Wallet> = MutableLiveData()
    val walletLiveData: LiveData<Wallet> = _walletLiveData

    init {
        val transactionDao = DataBase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
    }

    //function to get all Wallets
    fun getWallet(cryptoType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _walletLiveData.postValue(repository.getWallet(cryptoType))
        }
    }
}