package com.example.pgr208_2021_android_exam.data.domain

import com.example.pgr208_2021_android_exam.data.CoinCapService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

//creates a moshiAdapter converting from JSON to Kotlin
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory()).build()

//creating retrofit-client for request/response to an REST API in Android
private val retroFit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl("https://api.coincap.io/v2/").build()

// Shared API-object that will be used to "access" the CoinCapService with
object CoinCapApi {
    val coinCapService: CoinCapService by lazy {
        retroFit.create(CoinCapService::class.java)
    }
}