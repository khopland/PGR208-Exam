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

    suspend fun addTransaction(t: Transaction): Boolean {
        return if (t.selling) selling(t)
        else buying(t)

    }

    private suspend fun buying(t: Transaction): Boolean {
        return try {
            if (getDollar() > t.dollar) {
                if (!dao.walletExists(t.cryptoType))
                    dao.insertWallet(Wallet(t.cryptoType, 0))
                reduceAmount("dollar", t.dollar)
                inciseAmount(t.cryptoType, ((t.dollar * t.conversionRate).toLong()))
                dao.insertTransaction(t)
                true
            } else false
        } catch (e: Error) {
            print(e)
            false
        }
    }


    private suspend fun selling(t: Transaction): Boolean {
        if (!dao.walletExists(t.cryptoType)) return false
        val changeValue = (t.dollar * t.conversionRate).toLong()
        return try {
            if (dao.getWalletByCryptoType(t.cryptoType).amount > changeValue) {
                inciseAmount("dollar", t.dollar)
                reduceAmount(t.cryptoType, (changeValue))
                dao.insertTransaction(t)
                true
            } else false
        } catch (e: Error) {
            print(e)
            false
        }
    }

    private suspend fun reduceAmount(cryptoType: String, amount: Long) {
        val oldWallet = dao.getWalletByCryptoType(cryptoType)
        dao.updateWallet(oldWallet.copy(amount = oldWallet.amount - amount))
    }

    private suspend fun inciseAmount(cryptoType: String, amount: Long) {
        val oldWallet = dao.getWalletByCryptoType(cryptoType)
        dao.updateWallet(oldWallet.copy(amount = oldWallet.amount + amount))
    }
}