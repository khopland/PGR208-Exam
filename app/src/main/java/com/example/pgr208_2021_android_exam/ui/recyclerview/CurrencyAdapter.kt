package com.example.pgr208_2021_android_exam.ui.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pgr208_2021_android_exam.data.domain.CryptoCurrency
import com.example.pgr208_2021_android_exam.data.getImg
import com.example.pgr208_2021_android_exam.databinding.OverviewCurrencyItemBinding
import kotlin.math.round

class CurrencyAdapter(
    private val context: Context,
    private val cryptoCurrencyList: List<CryptoCurrency>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        // Create a binding of the inflated "overview_currency_item.xml"-file
        val binding =
            OverviewCurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
    class CurrencyViewHolder(
        private val context: Context,
        private val binding: OverviewCurrencyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(cryptoCurrency: CryptoCurrency) {
            // symbol (BTC)
            // name (Bitcoin)
            // priceInUSD (the value in double)
            // changePercentInLast24Hr (positive / negative percentage change)
            val change24h = "${rounding(cryptoCurrency.changePercentInLast24Hr)}%"

            binding.apply {
                tvCurrencySymbol.text = cryptoCurrency.symbol
                tvCurrencyName.text = cryptoCurrency.name
                tvCurrencyRate.text = "\$${rounding(cryptoCurrency.priceInUSD)}"
                tvCurrencyPercentageUpdate.text = change24h
                if (change24h.startsWith("-"))
                    tvCurrencyPercentageUpdate.setTextColor(Color.parseColor("#FF0000"))
                else
                    tvCurrencyPercentageUpdate.setTextColor(Color.parseColor("#00FF00"))

            }
            getImg(context, cryptoCurrency.symbol, binding.ivCurrencyIcon)
        }

        private fun rounding(d: Double): Double {
            return round((d * 100)) / 100
        }
    }

    // This part is taken from the following Kotlin Codelab:
    // https://developer.android.com/codelabs/kotlin-android-training-internet-images?index=..%2F..android-kotlin-fundamentals#0
    class OnClickListener(val clickListener: (cryptoCurrency: CryptoCurrency) -> Unit) {
        fun onClick(cryptoCurrency: CryptoCurrency) = clickListener(cryptoCurrency)
    }

}