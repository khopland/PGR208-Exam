package com.example.pgr208_2021_android_exam.data.domain


import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.pgr208_2021_android_exam.ui.recyclerview.CurrencyAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.MathContext
import java.math.RoundingMode
import java.util.*
import kotlin.math.roundToLong



// NB: Same reason as above, but this time "data" is an array with nested objects...
@JsonClass(generateAdapter = true)
data class CryptoListData(
    @Json(name = "data")
    val cryptoCurrencies: List<Crypto>

)

// NB: We had to make a separate data class here because of the JSON-structure in the response from CoinCap
/* Single bitcoin from CoinCap (wrapped in data)
{
  "data": {
    "id": "bitcoin",
    "rank": "1",
    "symbol": "BTC",
    "name": "Bitcoin",
    "supply": "18672456.0000000000000000",
    "maxSupply": "21000000.0000000000000000",
    "marketCapUsd": "1117948765866.7382456920523808",
    "volumeUsd24Hr": "17694730163.6919540300382850",
    "priceUsd": "59871.5437255141072868",
    "changePercent24Hr": "0.2894664016367318",
    "vwap24Hr": "60119.9562425723390902",
    "explorer": "https://blockchain.info/"
  },
  "timestamp": 1618241031286
}
 */
@JsonClass(generateAdapter = true)
data class CryptoData(
        @Json(name = "data")
        val crypto: Crypto

)

// Each crypto-currency wrapped in a "data"-field
@JsonClass(generateAdapter = true)
data class Crypto(
    val id: String,
    val rank: String,
    val symbol: String,
    val name: String,
    val supply: String,
    val maxSupply: String?,
    val marketCapUsd: String,
    val volumeUsd24Hr: String,
    val priceUsd: String,
    val changePercent24Hr: String,
    val vwap24Hr: String?,
    val explorer: String

)


// Conversion from CryptoData to our domain model
fun CryptoData.toDomainModel(): CryptoCurrency {

    // Destructuring out the crypto-property from the "CryptoData"-data class

    val (crypto) = this

    return CryptoCurrency(
        type = crypto.id,
        symbol = crypto.symbol,
        name = crypto.name,
        priceInUSD = crypto.priceUsd.toDouble(),
        changePercentInLast24Hr = crypto.changePercent24Hr.toDouble(),
        supply = crypto.supply.toDouble().toLong()
    )
}

// Conversion from CryptoListData to a list in our domain model
fun CryptoListData.toDomainModel(): List<CryptoCurrency> {

    val (cryptoCurrencies) = this

    return cryptoCurrencies.map { crypto ->
        CryptoCurrency(
            type = crypto.id,
            symbol = crypto.symbol,
            name = crypto.name,
            priceInUSD = crypto.priceUsd.toDouble(),
            changePercentInLast24Hr = crypto.changePercent24Hr.toDouble(),
            supply = crypto.supply.toDouble().toLong()

        )
    }

}

//function toDomain where we splits the list in

// function for converting/getting a Crypto-instance that has the required-format when requesting data via ConCapService
// e.g: Crypto will have a String id, which is the lower-cased name of the cryptoCurrency, but on the domainModel that value is named "type".
fun fromDomainModel(model: CryptoCurrency): Crypto {
    return Crypto(
        id = model.type,
        rank = "",
        symbol = model.symbol,
        name = model.name,
        supply = model.supply.toDouble().toString(),
        maxSupply = "",
        marketCapUsd = "",
        volumeUsd24Hr = "",
        priceUsd = model.priceInUSD.toString(),
        changePercent24Hr = model.changePercentInLast24Hr.toString(),
        vwap24Hr = "",
        explorer = ""

    )
}

// Our domain-model (the data we want to work with in our domain)
// NB: I think that
data class CryptoCurrency(
        val type: String,
        val symbol: String,
        val name: String,
        val priceInUSD: Double,
        val changePercentInLast24Hr: Double,
        val supply: Long,
)

//fetches icon from static.coincap.io and makes
fun getImg(context:Context, cryptoType: String, icon: ImageView) {
        Glide.with(context)
                .load("https://static.coincap.io/assets/icons/${cryptoType.toLowerCase(Locale.ROOT)}@2x.png")
                .fitCenter()
                .into(icon)
}

