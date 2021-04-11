package com.example.pgr208_2021_android_exam.database.entities

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @NonNull
    @ColumnInfo(name = "selling")
    //if you are selling set to true
    val selling: Boolean,
    @NonNull
    @ColumnInfo(name = "dollar")
    //payment in dollar
    val dollar: Long,
    @NonNull
    @ColumnInfo(name = "conversion_rate")
    //the conversion rate for the specific crypto
    val conversionRate: Double,
    @NonNull
    @ColumnInfo(name = "crypto_type")
    //the name of crypto
    val cryptoType: String,
    @NonNull
    @ColumnInfo(name = "time_date")
    val timeDate: String
)