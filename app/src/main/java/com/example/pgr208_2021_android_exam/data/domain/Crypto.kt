package com.example.pgr208_2021_android_exam.data.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

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

/* NB: Contains cryptoCurrency and regular currency (fiat-currency)

    {
      "data": {
        "id": "bitcoin",
        "symbol": "BTC",
        "currencySymbol": "₿",
        "type": "crypto",
        "rateUsd": "6444.3132749056076909"
      },
      "timestamp": 1536347871542
    }

*/
@JsonClass(generateAdapter = true)
data class CurrencyRatesListData(
        @Json(name = "data")
        val rates: List<CurrencyRate>
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
        val explorer: String?
)

/*
    {
        "id": "bitcoin",
        "symbol": "BTC",
        "currencySymbol": "₿",
        "type": "crypto",
        "rateUsd": "6444.3132749056076909"
      },
 */

@JsonClass(generateAdapter = true)
data class CurrencyRate(
        val id: String,
        val symbol: String,
        val currencySymbol: String?,
        val type: String,
        val rateUsd: String,
)

// Conversion from CryptoData to our domain model
fun CryptoData.toDomainModel(): CryptoCurrency {

    // Destructuring out the crypto-property from the "CryptoData"-data class
    val ( crypto ) = this

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

// Conversion from list of all rates (cryptoCurrency and regular currency) -
// to a list of only cryptoCurrency-rates ( and USD :] ) in our domain-model
fun CurrencyRatesListData.toDomainModel(): List<CoinRate> {

    val (rates) = this

    // Rate for a USD as a currency
    val dollarRate = rates.find { it.symbol == "USD" }

    val cryptoRates: MutableList<CurrencyRate> = mutableListOf()

    // Chose to use if-check here as the "?.let"-variant wasn't as readable...
    if (dollarRate != null) {
        cryptoRates.add(dollarRate)
    }

    // Add in all the crypto-rates to the list
    cryptoRates.addAll(rates.filter { it.type == "crypto" })

    // Transform the cryptoRates into objects in our domain-model
    return cryptoRates.map { rate ->
        CoinRate(
                name = rate.id,
                symbol = rate.symbol,
                type = rate.type,
                rateUSD = rate.rateUsd.toDouble()
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
// Adding parcelize so we can send the cryptoCurrency as intent or bundle etc.
@Parcelize
data class CryptoCurrency(
        val type: String,
        val symbol: String,
        val name: String,
        val priceInUSD: Double,
        val changePercentInLast24Hr: Double,
        val supply: Long,
) : Parcelable

/* Examples below for each "type"

    // Example for a "fiat-type" coin
    {
        "id": "united-states-dollar",
        "symbol": "USD",
        "currencySymbol": "$",
        "type": "fiat",
        "rateUsd": "1.0000000000000000"
    }

    // Example for a "crypto-type" coin
    {
        "id": "bitcoin",
        "symbol": "BTC",
        "currencySymbol": "₿",
        "type": "crypto",
        "rateUsd": "6444.3132749056076909"
      },

      (NB: only Bitcoin and USD have a non-null value for CurrencySymbol...)
 */
@Parcelize
data class CoinRate(
        val name: String,
        val symbol: String,
        val type: String,
        val rateUSD: Double,
) : Parcelable