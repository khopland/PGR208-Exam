package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.TransactionRepository
import com.example.pgr208_2021_android_exam.database.entities.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository

    private val _transactionLiveData: MutableLiveData<List<Transaction>> = MutableLiveData()
    val transactionLiveData: LiveData<List<Transaction>> = _transactionLiveData
    val successLiveData: MutableLiveData<Boolean?> = MutableLiveData()
/*
yourViewModelObject.status.observe(this, Observer { status ->
    status?.let {
        //Reset status value at first to prevent multitriggering
        //and to be available to trigger action again
        yourViewModelObject.status.value = null
        //Display Toast or snackbar
    }
})
*/

    init {
        val transactionDao = DataBase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
        getAllTransaction()
    }

    //function to generate a new transaction
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            successLiveData.postValue(repository.addTransaction(transaction))
        }
    }

    //function to get all transactions
    fun getAllTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            _transactionLiveData.value = repository.getAllTransactions()
        }
    }
}