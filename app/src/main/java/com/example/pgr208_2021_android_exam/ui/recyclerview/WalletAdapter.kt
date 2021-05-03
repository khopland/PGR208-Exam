package com.example.pgr208_2021_android_exam.ui.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208_2021_android_exam.data.getImg
import com.example.pgr208_2021_android_exam.databinding.PortfolioCurrencyItemBinding
import com.example.pgr208_2021_android_exam.ui.viewmodels.OwnedWallet

class WalletAdapter(
        private val context: Context,
        private val list: List<OwnedWallet>) : RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val binding = PortfolioCurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val wallet = list[position]

        holder.apply {
            this.bind(wallet)
        }
    }

    override fun getItemCount(): Int = list.size

    class WalletViewHolder(
            private val context: Context,
            private val binding: PortfolioCurrencyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        // TODO: Make resource-strings with placeholders...
        fun bind(wallet: OwnedWallet) {

            // TODO: Discuss what we want to do when a user sell all their owned crypto of a type (e.g: end up with 0.0 of DOGECOIN etc.)
            // Hide the given OwnedWallet if it's amount is less than 1
            this.itemView.visibility = if (wallet.amount > 1) View.VISIBLE else View.GONE

            binding.apply {
                getImg(context = context, cryptoType = wallet.cryptoType, binding.ivCurrencyIcon)
                tvCurrencyVolume.text = "Amount: ${wallet.amount}"
                tvCurrencyRate.text = "Rate: ${wallet.currencyRate}"
                tvCurrencyTotalValue.text = "Total: ${wallet.totalInUSD} USD"
            }
        }
    }
}