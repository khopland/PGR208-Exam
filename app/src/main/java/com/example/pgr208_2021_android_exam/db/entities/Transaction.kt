package com.example.pgr208_2021_android_exam.db.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "transaction_table")
class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @NonNull
    @ColumnInfo(name = "selling")
    val selling: Boolean,
    @NonNull
    @ColumnInfo(name = "amount")
    val amount: Long,
    @NonNull
    @ColumnInfo(name = "conversion_rate")
    val conversionRate: Long,
    @NonNull
    @ColumnInfo(name = "crypto_type")
    val cryptoType: Wallet,
    @NonNull
    @ColumnInfo(name = "time_date")
    val timeDate: LocalDateTime
    )