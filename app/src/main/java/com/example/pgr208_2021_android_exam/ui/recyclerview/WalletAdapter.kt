package com.example.pgr208_2021_android_exam.ui.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208_2021_android_exam.data.getImg
import com.example.pgr208_2021_android_exam.databinding.PortfolioCurrencyItemBinding
import com.example.pgr208_2021_android_exam.ui.viewmodels.OwnedWallet

class WalletAdapter(private val list: List<OwnedWallet>) :
    RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val binding =
            PortfolioCurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(parent.context, binding)
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
        fun bind(wallet: OwnedWallet) {
            binding.apply {
                getImg(context = context, cryptoType = wallet.cryptoType, ivCurrencyIcon)
                tvCurrencyVolume.text = "Amount: ${wallet.amount}"
                tvCurrencyRate.text = "Rate: ${wallet.currencyRate}"
                tvCurrencyTotalValue.text = "Total: ${wallet.totalInUSD} USD"
            }
        }
    }
}