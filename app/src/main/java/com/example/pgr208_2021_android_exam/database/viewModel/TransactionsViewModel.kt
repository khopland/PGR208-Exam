package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.TransactionRepository
import com.example.pgr208_2021_android_exam.database.entities.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository

    private val _transactionLiveData: MutableLiveData<List<Transaction>> = MutableLiveData()
    val transactionLiveData: LiveData<List<Transaction>> = _transactionLiveData

    init {
        val transactionDao = DataBase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        getAllTransaction()
    }

    //function to get all transactions
    fun getAllTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            _transactionLiveData.postValue(repository.getAllTransactions())
        }
    }
}