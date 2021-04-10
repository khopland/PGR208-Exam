package com.example.pgr208_2021_android_exam.database.db

import com.example.pgr208_2021_android_exam.database.entities.Transaction

class TransactionRepository(private val transactionDAO: TransactionDAO) {
    val readAllTransaction = transactionDAO.fetchAll()

    suspend fun addTransaction(t:Transaction){
        transactionDAO.insert(t)
    }
}