package com.example.pgr208_2021_android_exam.ui.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208_2021_android_exam.R
import com.example.pgr208_2021_android_exam.data.domain.CryptoCurrency
import com.example.pgr208_2021_android_exam.data.domain.getImg

import com.example.pgr208_2021_android_exam.databinding.OverviewCurrencyItemBinding

class CurrencyAdapter(private val context: Context, private val cryptoCurrencyList: List<CryptoCurrency>, val onClickListener: OnClickListener): RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        // Create a binding of the inflated "overview_currency_item.xml"-file
        val binding = OverviewCurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(context, binding)
    }

    // method binds the data from the currency list
    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val cryptoCurrency = cryptoCurrencyList[position]

        holder.apply {
            this.itemView.setOnClickListener { onClickListener.onClick(cryptoCurrency) }
            this.bind(cryptoCurrency)
        }
    }

    override fun getItemCount(): Int = cryptoCurrencyList.size

    //class is holds the list view
    class CurrencyViewHolder(private val context: Context, private val binding: OverviewCurrencyItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(cryptoCurrency: CryptoCurrency) {
            // symbol (BTC)
            // name (Bitcoin)
            // priceInUSD (the value in double)
            // changePercentInLast24Hr (positive / negative percentage change)
            binding.apply {
                tvCurrencySymbol.text = cryptoCurrency.symbol
                tvCurrencyName.text = cryptoCurrency.name
                tvCurrencyRate.text = cryptoCurrency.priceInUSD.toString()
                tvCurrencyPercentageUpdate.text = cryptoCurrency.changePercentInLast24Hr.toString()
            }

            getImg(context = context, cryptoType = cryptoCurrency.symbol, icon = binding.ivCurrencyIcon)
        }
    }

    // This part is taken from the following Kotlin Codelab:
    // https://developer.android.com/codelabs/kotlin-android-training-internet-images?index=..%2F..android-kotlin-fundamentals#0
    class OnClickListener(val clickListener: (cryptoCurrency: CryptoCurrency) -> Unit) {
        fun onClick(cryptoCurrency: CryptoCurrency) = clickListener(cryptoCurrency)
    }

}