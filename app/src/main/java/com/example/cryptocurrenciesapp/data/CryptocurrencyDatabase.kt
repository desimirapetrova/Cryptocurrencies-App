package com.example.cryptocurrenciesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cryptocurrenciesapp.data.dao.CryptocurrencyDao
import com.example.cryptocurrenciesapp.data.entity.CryptocurrencyEntity

@Database(entities = [CryptocurrencyEntity::class], version = 1)
abstract class CryptocurrencyDatabase: RoomDatabase() {
    abstract  fun cryptocurrencyDao():CryptocurrencyDao
}