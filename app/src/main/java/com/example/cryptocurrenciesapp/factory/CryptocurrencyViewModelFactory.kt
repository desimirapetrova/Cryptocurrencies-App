package com.example.cryptocurrenciesapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptocurrenciesapp.data.dao.CryptocurrencyDao
import com.example.cryptocurrenciesapp.repository.CryptocurrencyRepository
import com.example.cryptocurrenciesapp.viewModel.CryptocurrencyViewModel

class CryptocurrencyViewModelFactory(
    private val cryptocurrencyRepository: CryptocurrencyRepository
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CryptocurrencyViewModel(cryptocurrencyRepository) as T
    }
}