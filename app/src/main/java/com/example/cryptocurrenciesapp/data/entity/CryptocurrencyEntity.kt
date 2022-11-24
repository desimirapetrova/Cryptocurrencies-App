package com.example.cryptocurrenciesapp.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cryptocurrencies")
data class CryptocurrencyEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "symbol") var symbol: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "current_price") var currentPrice: Float,
    @ColumnInfo(name = "market_cap") var market_cap: Long,
    @ColumnInfo(name = "high_24h") var high_24h: Float,
    @ColumnInfo(name = "price_change_percentage_24h") var price_change_percentage_24h: Float,
    @ColumnInfo(name = "market_cap_change_percentage_24h") var market_cap_change_percentage_24h: Float,
    @ColumnInfo(name = "low_24h") var low_24h: Float,
    @ColumnInfo(name = "favourite") var favourite: Boolean=false
)
