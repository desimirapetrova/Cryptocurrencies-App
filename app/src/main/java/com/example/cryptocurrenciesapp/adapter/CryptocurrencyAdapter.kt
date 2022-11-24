package com.example.cryptocurrenciesapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrenciesapp.R
import com.example.cryptocurrenciesapp.activity.MainActivity
import com.example.cryptocurrenciesapp.data.CryptocurrencyDatabase
import com.example.cryptocurrenciesapp.data.entity.CryptocurrencyEntity
import com.example.cryptocurrenciesapp.databinding.CryptocurrencyItemBinding
import com.example.cryptocurrenciesapp.fragment.CryptocurrencyFragment
import java.util.*

class CryptocurrencyAdapter(private val cryptocurrencies:List<CryptocurrencyEntity>):

    RecyclerView.Adapter<CryptocurrencyAdapter.CryptocurrencyViewHolder>() {
    var db: CryptocurrencyDatabase? = null

    class CryptocurrencyViewHolder(val binding: CryptocurrencyItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptocurrencyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CryptocurrencyItemBinding.inflate(layoutInflater, parent, false)

        return CryptocurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CryptocurrencyViewHolder, position: Int) {
        val currentCryptoCurrency = cryptocurrencies[position]
        holder.binding.apply {
            name = currentCryptoCurrency.name
            symbol = currentCryptoCurrency.symbol.uppercase(Locale.ROOT)
            currentPrice ="${currentCryptoCurrency?.currentPrice} USD"
            ivLiked.visibility = if (currentCryptoCurrency.favourite) View.VISIBLE else View.GONE

            Glide
                .with(this.root.context)
                .load(currentCryptoCurrency.image)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(ivLogo)
        }

        holder.binding.root.setOnClickListener {
            (it.context as MainActivity).supportFragmentManager.commit {
                val bundle = Bundle();
                bundle.putString("cryptocurrency_id", currentCryptoCurrency.id)
                replace(R.id.container_view, CryptocurrencyFragment::class.java, bundle)
                addToBackStack("cryptocurrency_list_to_details")
            }

        }
    }

    override fun getItemCount() = cryptocurrencies.size

}