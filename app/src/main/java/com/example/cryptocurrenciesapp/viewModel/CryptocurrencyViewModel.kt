package com.example.cryptocurrenciesapp.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.cryptocurrenciesapp.data.dao.CryptocurrencyDao
import com.example.cryptocurrenciesapp.data.entity.CryptocurrencyEntity
import com.example.cryptocurrenciesapp.model.CryptocurrencyResponse
import com.example.cryptocurrenciesapp.repository.CryptocurrencyRepository
import com.example.cryptocurrenciesapp.service.CryptocurrencyService
import com.example.cryptocurrenciesapp.util.NetworkUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CryptocurrencyViewModel(
    private val cryptocurrencyRepository: CryptocurrencyRepository
) : ViewModel() {

    private val cryptocurrenciesListStateFlow =
        MutableStateFlow<List<CryptocurrencyEntity>>(arrayListOf())
    private val selectedCryptocurrencyStateFlow = MutableStateFlow<CryptocurrencyEntity?>(null)
    val cryptocurrenciesList: StateFlow<List<CryptocurrencyEntity>> =
        cryptocurrenciesListStateFlow.asStateFlow()
    val selectedCryptocurrency: StateFlow<CryptocurrencyEntity?> =
        selectedCryptocurrencyStateFlow.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getCountries() {
        val countries = cryptocurrencyRepository.getCryptocurrencies()
        cryptocurrenciesListStateFlow.value = countries
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getCountryByName(name: String) {
        val country = cryptocurrencyRepository.getCrypto(name)
        selectedCryptocurrencyStateFlow.value = country ?: return
    }

    suspend fun updateFavourites(country: CryptocurrencyEntity) {
        cryptocurrencyRepository.updateCountry(country)
        selectedCryptocurrencyStateFlow.value =
            selectedCryptocurrencyStateFlow.value?.copy(favourite = country.favourite)
    }
}