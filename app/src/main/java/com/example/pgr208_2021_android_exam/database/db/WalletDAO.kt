package com.example.pgr208_2021_android_exam.database.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pgr208_2021_android_exam.database.entities.Wallet

@Dao
interface WalletDAO {
    @Insert
    suspend fun insert(wallet: Wallet)

    @Update
    suspend fun update(wallet: Wallet)

    @Query("SELECT * FROM wallet_table")
    suspend fun fetchAll(): List<Wallet>
}