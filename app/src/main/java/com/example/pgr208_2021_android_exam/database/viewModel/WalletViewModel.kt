package com.example.pgr208_2021_android_exam.database.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pgr208_2021_android_exam.database.db.DataBase
import com.example.pgr208_2021_android_exam.database.db.WalletRepository

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WalletRepository


    init {
        val walletDAO = DataBase.getDatabase(application).walletDao()
        repository = WalletRepository(walletDAO)
    }
}