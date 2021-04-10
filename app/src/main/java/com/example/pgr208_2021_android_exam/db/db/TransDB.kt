package com.example.pgr208_2021_android_exam.db.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
/*
@Database(entities = [Transaction::class], version = 1)
abstract class TransDB : RoomDatabase() {
    abstract fun transactionDao(): TransactionDAO

    companion object {
        private var db: TransDB? = null

        fun getDatabase(context: Context): TransDB {
            val newDb =
                db ?: Room.databaseBuilder(context, TransDB::class.java, "stonks_database")
                    .build()
            return newDb.also {
                db = it
            }
        }
    }
}*/
