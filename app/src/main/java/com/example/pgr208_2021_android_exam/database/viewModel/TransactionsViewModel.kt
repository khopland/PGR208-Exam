package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.example.pgr208_2021_android_exam.data.rounding
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.TransactionRepository
import com.example.pgr208_2021_android_exam.database.entities.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    private fun getAllTransaction() {
        viewModelScope.launch(Dispatchers.IO) {
            _transactionLiveData.postValue(repository.getAllTransactions())
        }
    }

    /* UI related functionality */
    val userTransactions = transactionLiveData.map { transactions ->

        transactions.map {

            val amount = rounding(it.dollar / it.conversionRate)

            val summary = if (it.cryptoType == "USD") "${it.dollar} $"
            else "$amount ${it.cryptoType} for ${it.dollar} USD"

            // Found a little info on this here: https://www.ictdemy.com/kotlin/oop/date-and-time-in-kotlin-parsing-and-comparing
            val format = LocalDateTime.parse(it.timeDate)
            val timeDate = format.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))

            UserTransaction(
                message = it.message,
                summary = summary,
                timeDate = timeDate,
                cryptoType = it.cryptoType
            )
        }
    }
}

// Data class for presentation of transactions (buy/sell) done by the user
data class UserTransaction(
    val message: String,
    val summary: String,
    val timeDate: String,
    val cryptoType: String,
)