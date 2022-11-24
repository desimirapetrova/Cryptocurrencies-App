package com.example.cryptocurrenciesapp.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptocurrenciesapp.R
import com.example.cryptocurrenciesapp.adapter.CryptocurrencyAdapter
import com.example.cryptocurrenciesapp.data.CryptocurrencyDatabase
import com.example.cryptocurrenciesapp.databinding.ActivityMainBinding
import com.example.cryptocurrenciesapp.factory.CryptocurrencyViewModelFactory
import com.example.cryptocurrenciesapp.repository.CryptocurrencyRepository
import com.example.cryptocurrenciesapp.service.CryptocurrencyService
import com.example.cryptocurrenciesapp.util.NetworkUtil
import com.example.cryptocurrenciesapp.viewModel.CryptocurrencyViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var cryptocurrencyService: CryptocurrencyService

    private lateinit var cryptocurrencyRepository: CryptocurrencyRepository

    lateinit var cryptocurrencyViewModel: CryptocurrencyViewModel

    lateinit var db: RoomDatabase

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.coingecko.com/api/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()

    //    @RequiresApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        init()
        if (!NetworkUtil.isConnected(this)) {
            Snackbar.make(
                binding.root,
                "No internet connection, information could be outdated",
                Snackbar.LENGTH_LONG
            ).show()
        }

        GlobalScope.launch {
            cryptocurrencyViewModel.getCountries()
        }
        observeData()
        setContentView(binding.root)

//        cryptocurrencyViewModel.getCryptocurrencies()
//        val isOnline= isOnline(applicationContext)
//        if(!isOnline){
//            cryptocurrencyViewModel.getCryptocurrenciesFromDb();
//        }
    }

    private fun init() {
        db = Room.databaseBuilder(
            applicationContext,
            CryptocurrencyDatabase::class.java,
            "cryptocurrencies"
        ).build()
        val cryptocurrencyDao = (db as CryptocurrencyDatabase).cryptocurrencyDao()

        cryptocurrencyService = retrofit.create(CryptocurrencyService::class.java)
        cryptocurrencyRepository =
            CryptocurrencyRepository(this, cryptocurrencyService, cryptocurrencyDao)
        val countryModelFactory = CryptocurrencyViewModelFactory(cryptocurrencyRepository)
        cryptocurrencyViewModel =
            ViewModelProvider(this, countryModelFactory)[CryptocurrencyViewModel::class.java]
    }

    private fun observeData() {
        GlobalScope.launch {
            cryptocurrencyViewModel.cryptocurrenciesList.collect {
                runOnUiThread {
                    val cryptoMarkets = it

                    val sortedCryptoMarketsByMarketCap = cryptoMarkets.sortedByDescending { it.market_cap }
                    val sortedCryptoMarkets = sortedCryptoMarketsByMarketCap.sortedByDescending { it.favourite }
                    val adapter = CryptocurrencyAdapter(sortedCryptoMarkets)
                    binding.tvCryptocurrenciesCountList.adapter = adapter
                    binding.tvCryptoCount.text = "Crypto Markets: ${it.size}"

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return true
        }
        return false
    }
}