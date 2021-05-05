package com.example.pgr208_2021_android_exam.data.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/* CurrencyRates (regular and crypto) */

/* NB: Contains cryptoCurrency and regular currency (fiat-currency)

    {
      "data":
      {
        "id": "bitcoin",
        "symbol": "BTC",
        "currencySymbol": "₿",
        "type": "crypto",
        "rateUsd": "6444.3132749056076909"
      },
      "timestamp": 1536347871542
    }

    {
      "data" :
      {
        "id": "united-states-dollar",
        "symbol": "USD",
        "currencySymbol": "$",
        "type": "fiat",
        "rateUsd": "1.0000000000000000"

      }
    }
*/

@JsonClass(generateAdapter = true)
data class CurrencyRateData(
    @Json(name = "data")
    val rate: CurrencyRate
)

/* Example
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

// Transform the cryptoRates into objects in our domain-model
fun CurrencyRateData.toDomainModel(): CoinRate {

    val (rate) = this

    return CoinRate(
        name = rate.id,
        symbol = rate.symbol,
        type = rate.type,
        rateUSD = rate.rateUsd.toDouble()
    )
}

// For use with "getAllRates" to transform list of currencies into "map of rates for cryptoCurrencies"
fun fromCryptoCurrenciesToCoinRates(currencies: List<CryptoCurrency>): Map<String, CoinRate> {
    val rates = mutableMapOf<String, CoinRate>()

    /* "Conversion table" for CryptoCurrency to CoinRate-mapping

        CryptoCurrency to CoinRate
            type => name
            symbol => symbol
            ... => type (always "crypto")
            priceInUSD => rateUSD
     */
    // Found info about the practical use of this, from the following link: https://www.baeldung.com/kotlin/list-to-map#3-the-associatebyto-method
    currencies.associateByTo(rates, { it.symbol }, {
        CoinRate(
            name = it.type,
            symbol = it.symbol,
            type = "crypto",
            rateUSD = it.priceInUSD
        )
    }
    )

    return rates
}


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