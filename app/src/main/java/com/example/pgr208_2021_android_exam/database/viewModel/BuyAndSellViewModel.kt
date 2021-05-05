package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pgr208_2021_android_exam.data.rounding
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.TransactionRepository
import com.example.pgr208_2021_android_exam.database.entities.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BuyAndSellViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository

    val successLiveData: MutableLiveData<Boolean?> = MutableLiveData(null)
    /*
    BuyAndSellViewModel.successLiveData.observe(this, Observer { status ->
        status?.let {
            //Reset status value at first to prevent multitriggering
            //and to be available to trigger action again
            BuyAndSellViewModel.successLiveData.value = null
            //Display Toast or snackbar
        }
    })
    */

    init {
        val transactionDao = DataBase.getDatabase(application).transactionDao()
        repository = TransactionRepository(transactionDao)
    }

    //function to generate a new transaction
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            successLiveData.postValue(repository.addTransaction(transaction))
        }
    }

    /* UI related functionality */

    fun buy(isSelling: Boolean, dollar: Long, conversionRate: Double, cryptoType: String) {

        // Create a transaction and call addTransaction (store result in isSuccess)
        val transaction = Transaction(
            id = 0,
            message = "BOUGHT",
            selling = isSelling,
            dollar = dollar,
            conversionRate = rounding(conversionRate),
            cryptoType = cryptoType,
            timeDate = LocalDateTime.now().toString()
        )

        addTransaction(transaction)
    }

    fun sell(isSelling: Boolean, dollar: Long, conversionRate: Double, cryptoType: String) {

        // Create a transaction and call addTransaction (store result in isSuccess)
        val transaction = Transaction(
            id = 0,
            message = "SOLD",
            selling = isSelling,
            dollar = dollar,
            conversionRate = rounding(conversionRate),
            cryptoType = cryptoType,
            timeDate = LocalDateTime.now().toString()
        )

        addTransaction(transaction)
    }

}