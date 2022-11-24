package com.example.cryptocurrenciesapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.annotation.RequiresApi

object NetworkUtil {

    @RequiresApi(Build.VERSION_CODES.M)
    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            return if (capabilities.hasTransport(TRANSPORT_CELLULAR)) {
                true
            } else if (capabilities.hasTransport(TRANSPORT_WIFI)) {
                true
            } else capabilities.hasTransport(TRANSPORT_ETHERNET)
        }

        return false
    }
}