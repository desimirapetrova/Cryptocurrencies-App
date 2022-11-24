package com.example.cryptocurrenciesapp.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cryptocurrenciesapp.data.dao.CryptocurrencyDao
import com.example.cryptocurrenciesapp.data.entity.CryptocurrencyEntity
import com.example.cryptocurrenciesapp.model.CryptocurrencyResponse
import com.example.cryptocurrenciesapp.service.CryptocurrencyService
import com.example.cryptocurrenciesapp.util.NetworkUtil
import retrofit2.Call

class CryptocurrencyRepository constructor(
    private val context: Context,
    private val cryptocurrencyService: CryptocurrencyService,
    private val cryptocurrencyDao: CryptocurrencyDao
) {

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getCryptocurrencies(): List<CryptocurrencyEntity> {
        return try {
            // if Internet connection is available fetch countries, save them to DB and return them
            val savedinDbMarkets = cryptocurrencyDao.getAllCryptocurrencies()

            if (NetworkUtil.isConnected(context)) {
                // execute the network request synchronously in order to wait for the response and handle it
                val countries = cryptocurrencyService.getCryptocurrencies().execute().body()
                val roomCountries = countries?.map { mapResponseToDbModel(it) }
                roomCountries?.forEach { new ->
                    var oldCryptoDatabaseModel = savedinDbMarkets.find { it.id == new.id }
                    // We only know that there is one property that may be different
                    if (oldCryptoDatabaseModel != null && oldCryptoDatabaseModel.favourite != new.favourite) {
                        new.favourite = oldCryptoDatabaseModel.favourite
                    }
                }
                cryptocurrencyDao.insertAll(roomCountries ?: return arrayListOf())

            }

            cryptocurrencyDao.getAllCryptocurrencies()
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getCrypto(name: String): CryptocurrencyEntity? {
        return try {
            // if Internet connection is available fetch countries, save them to DB and return them
            if (NetworkUtil.isConnected(context)) {
                // execute the network request synchronously in order to wait for the response and handle it
                val countries = cryptocurrencyService.getCryptoById(name).execute().body()
                val roomCountries = countries?.map { mapResponseToDbModel(it) }
                cryptocurrencyDao.insertAll(roomCountries ?: return null)
            }

            return cryptocurrencyDao.getCountryByName(name)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateCountry(country: CryptocurrencyEntity) {
        cryptocurrencyDao.update(country)
    }

    private fun mapResponseToDbModel(response: CryptocurrencyResponse): CryptocurrencyEntity {
        return CryptocurrencyEntity(
            id = response.id,
            name = response.name?:"",
            symbol = response.symbol?:"",
            image = response.image?:"",
            currentPrice = response.current_price,
            market_cap = response.market_cap,
            high_24h = response.high_24h,
            price_change_percentage_24h = response.price_change_percentage_24h,
            market_cap_change_percentage_24h = response.market_cap_change_percentage_24h,
            low_24h = response.low_24h,
            favourite = false
        )
    }

}