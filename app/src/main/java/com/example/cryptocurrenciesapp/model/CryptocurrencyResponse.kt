package com.example.cryptocurrenciesapp.model

data class CryptocurrencyResponse(
    var id:String,
    var symbol:String,
    var name:String,
    val image:String,
    var current_price:Float,
    var market_cap:Long,
    var high_24h:Float,
    var price_change_percentage_24h:Float,
    var market_cap_change_percentage_24h:Float,
    var low_24h:Float,
    var favourite: Boolean
)