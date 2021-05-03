package com.example.pgr208_2021_android_exam.ui.recyclerview

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208_2021_android_exam.data.getImg
import com.example.pgr208_2021_android_exam.database.viewModel.UserTransaction
import com.example.pgr208_2021_android_exam.databinding.TransactionItemBinding

class TransactionsAdapter(private val list: List<UserTransaction>) : RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        val binding = TransactionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionsViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        val reversed = list.reversed()
        val userTransaction = reversed[position]

        holder.apply {
            this.bind(userTransaction)
        }
    }

    override fun getItemCount(): Int = list.size



    class TransactionsViewHolder(private val context: Context,
                                 private val binding: TransactionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: UserTransaction) {
            binding.apply {
                getImg(context, transaction.cryptoType, ivCurrencyIcon)

                val bought = transaction.message.startsWith("bought", ignoreCase = true)
                val installationReward = transaction.message.startsWith("installation", ignoreCase = true)

                if (bought || installationReward) tvMessage.setTextColor(Color.GREEN)
                else tvMessage.setTextColor(Color.MAGENTA)

                tvMessage.text = transaction.message
                tvSummary.text = transaction.summary
                tvTimestamp.text = transaction.timeDate
            }
        }
    }
}