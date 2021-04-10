package com.example.pgr208_2021_android_exam.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet")
class wallet(
    @PrimaryKey
    @ColumnInfo(name = "crypto_type")
    val cryptoType: String,

    @ColumnInfo(name = "sum")
    val sum: Long
)