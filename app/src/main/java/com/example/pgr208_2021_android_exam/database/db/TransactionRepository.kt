package com.example.pgr208_2021_android_exam.database.db

import com.example.pgr208_2021_android_exam.database.entities.Transaction
import com.example.pgr208_2021_android_exam.database.entities.Wallet

class TransactionRepository(private val dao: DAO) {
    suspend fun getAllTransactions(): List<Transaction> {
        return dao.fetchAllTransaction()
    }

    suspend fun getDollar(): Long {
        return dao.getWalletByCryptoType("dollar").amount
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
                    dao.insertWallet(Wallet(t.cryptoType, 0))
                //transfer money fom dollar to crypto amount
                reduceAmount(dao.getWalletByCryptoType("dollar"), t.dollar)
                increaseAmount(
                    dao.getWalletByCryptoType(t.cryptoType),
                    ((t.dollar * t.conversionRate).toLong())
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
        val changeValue = (t.dollar * t.conversionRate).toLong()
        return try {
            if (wallet.amount >= changeValue) {
                increaseAmount(dao.getWalletByCryptoType("dollar"), t.dollar)
                reduceAmount(wallet, (changeValue))
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
    private suspend fun increaseAmount(wallet: Wallet, amount: Long) {
        dao.updateWallet(wallet.copy(amount = wallet.amount + amount))
    }
}