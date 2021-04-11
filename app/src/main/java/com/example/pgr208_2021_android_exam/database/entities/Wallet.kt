package com.example.pgr208_2021_android_exam.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_table")
data class Wallet(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "crypto_type")
    val cryptoType: String,
    @NonNull
    @ColumnInfo(name = "amount")
    val amount: Long
)