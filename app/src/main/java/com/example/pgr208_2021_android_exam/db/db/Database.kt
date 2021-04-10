package com.example.pgr208_2021_android_exam.db.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction


const val DATABASE_NAME: String = "stonks_database"

@Database(entities = [Transaction::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDAO

    companion object {
        private var db: DataBase? = null

        fun getDatabase(context: Context): DataBase {
            val newDb =
                db ?: Room.databaseBuilder(context, DataBase::class.java, DATABASE_NAME).build()
            return newDb.also {
                db = it
            }
        }
    }
}