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

class AllWalletsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository

    private val _walletsLiveData: MutableLiveData<List<Wallet>> = MutableLiveData(null)
    val walletsLiveData: LiveData<List<Wallet>> = _walletsLiveData

    init {
        val transactionDao = DataBase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        getAllWallets()
    }

    //call this to refresh liveData
    fun getAllWallets() {
        viewModelScope.launch(Dispatchers.IO) {
            _walletsLiveData.postValue(repository.getAllWallets())
        }
    }
}