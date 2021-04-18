package com.example.pgr208_2021_android_exam.database.db

import android.util.Log
import com.example.pgr208_2021_android_exam.database.entities.Transaction
import com.example.pgr208_2021_android_exam.database.entities.Wallet
import java.time.LocalDateTime

class TransactionRepository(private val dao: DAO) {
    suspend fun getAllTransactions(): List<Transaction> {
        return dao.fetchAllTransaction()
    }

    suspend fun getWallet(cryptoType: String): Wallet {
        return dao.getWalletByCryptoType(cryptoType)
    }

    suspend fun getDollar(): Double {
        return dao.getWalletByCryptoType("USD").amount
    }

    //returns true if transaction went threw
    suspend fun addTransaction(t: Transaction): Boolean {
        return if (t.selling) selling(t)
        else buying(t)
    }

    //to handle transaction if you are buying
    private suspend fun buying(t: Transaction): Boolean {
        return try {
            if (getDollar() > t.dollar) {
                //to generate wallet if it dose not exist
                if (!dao.walletExists(t.cryptoType))
                    dao.insertWallet(Wallet(t.cryptoType, 0.0))
                //transfer money fom dollar to crypto amount
                reduceAmount(dao.getWalletByCryptoType("USD"), t.dollar)
                increaseAmount(
                    dao.getWalletByCryptoType(t.cryptoType),
                    ((t.dollar / t.conversionRate))
                )
                dao.insertTransaction(t)
                true
            } else false
        } catch (e: Error) {
            print(e)
            false
        }
    }

    //handle transaction if you are selling
    private suspend fun selling(t: Transaction): Boolean {
        if (!dao.walletExists(t.cryptoType)) return false
        val wallet = dao.getWalletByCryptoType(t.cryptoType)
        val changeValue = (t.dollar / t.conversionRate)
        return try {
            if (wallet.amount >= changeValue) {
                increaseAmount(dao.getWalletByCryptoType("USD"), t.dollar.toDouble())
                reduceAmount(wallet, changeValue.toLong())
                dao.insertTransaction(t)
                true
            } else false
        } catch (e: Error) {
            print(e)
            false
        }
    }

    //reduce amount from a specific account
    private suspend fun reduceAmount(wallet: Wallet, amount: Long) {
        dao.updateWallet(wallet.copy(amount = wallet.amount - amount))
    }

    //increase amount from a specific account
    private suspend fun increaseAmount(wallet: Wallet, amount: Double) {
        dao.updateWallet(wallet.copy(amount = wallet.amount + amount))
    }


    suspend fun start(): Boolean? {
        if (dao.fetchAllTransaction().isNullOrEmpty()) {
            val tempTransaction = Transaction(
                0,
                "Installation Reward",
                false,
                10_000,
                1.0,
                "USD",
                ""
            )
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    dao.insertTransaction(
                        tempTransaction.copy(
                            timeDate = LocalDateTime.now().toString()
                        )
                    )
                } else {
                    dao.insertTransaction(
                        tempTransaction.copy(
                            timeDate = (System.currentTimeMillis() / 1000).toString()
                        )
                    )
                }
                dao.insertWallet(
                    Wallet(
                        tempTransaction.cryptoType,
                        tempTransaction.dollar.toDouble()
                    )
                )
                return true
            } catch (e: Exception) {
                Log.e("Error with start transaction", e.toString())
            }
        }
        return null
    }

    suspend fun getAllWallets(): List<Wallet>? {
        return try {
            dao.getAllWallet()
        } catch (e: Exception) {
            Log.e("getAllWallets", e.toString(), e)
            null
        }
    }
}