package com.example.cryptocurrenciesapp.data.dao

import androidx.room.*
import com.example.cryptocurrenciesapp.data.entity.CryptocurrencyEntity

@Dao
interface CryptocurrencyDao {

    @Query("SELECT * FROM cryptocurrencies")
    fun getAllCryptocurrencies(): List<CryptocurrencyEntity>

    @Query("SELECT * FROM cryptocurrencies WHERE id=:id")
    suspend fun getCountryByName(id: String): CryptocurrencyEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(cryptocurrencies: List<CryptocurrencyEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(country: CryptocurrencyEntity)
}