package com.example.cryptocurrenciesapp.service

import com.example.cryptocurrenciesapp.model.CryptocurrencyResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptocurrencyService {

    @GET("coins/markets?vs_currency=usd")
    fun getCryptocurrencies(): Call<List<CryptocurrencyResponse>>

    @GET("coins/markets?vs_currency=usd")
    fun getCryptoById(@Query("ids")  id :String): Call<List<CryptocurrencyResponse>>
}