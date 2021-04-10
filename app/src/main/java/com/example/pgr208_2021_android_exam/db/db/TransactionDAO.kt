package com.example.pgr208_2021_android_exam.db.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.pgr208_2021_android_exam.db.entities.Transaction

@Dao
interface TransactionDAO {

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transaction_table")
    suspend fun fetchAll(): List<Transaction>

    @Query("select * from transaction_table where crypto_type = :cryptoType")
    suspend fun getTransactionByCrypto(cryptoType: String): List<Transaction>
}