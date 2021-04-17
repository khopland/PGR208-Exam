package com.example.pgr208_2021_android_exam.database.db

import androidx.room.*
import com.example.pgr208_2021_android_exam.database.entities.Transaction
import com.example.pgr208_2021_android_exam.database.entities.Wallet

@Dao
interface DAO {

    //Transaction
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM crypto_transaction_table")
    suspend fun fetchAllTransaction(): List<Transaction>

    //Wallet
    @Insert
    suspend fun insertWallet(wallet: Wallet)

    @Update
    suspend fun updateWallet(wallet: Wallet)

    @Query("SELECT EXISTS (SELECT 1 FROM wallet_table WHERE crypto_type = :ct)")
    suspend fun walletExists(ct: String): Boolean


    @Query("select * from wallet_table where crypto_type = :ct")
    suspend fun getWalletByCryptoType(ct: String): Wallet

    @Query("SELECT * FROM wallet_table")
    suspend fun getAllWallet(): List<Wallet>

}