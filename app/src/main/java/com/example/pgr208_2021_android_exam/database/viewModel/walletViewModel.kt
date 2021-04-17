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

class walletViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository

    private val _walletLiveData: MutableLiveData<List<Wallet>> = MutableLiveData()
    val walletLiveData: LiveData<List<Wallet>> = _walletLiveData

    init {
        val transactionDao = DataBase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        getAllWallets()
    }

    //function to get all Wallets
    fun getAllWallets() {
        viewModelScope.launch(Dispatchers.IO) {
            _walletLiveData.value = repository.getAllWallets()
        }
    }
}