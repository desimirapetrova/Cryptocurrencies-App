package com.example.cryptocurrenciesapp.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cryptocurrenciesapp.R
import com.example.cryptocurrenciesapp.activity.MainActivity
import com.example.cryptocurrenciesapp.databinding.FragmentCryptocurrencyDetailsBinding
import kotlinx.android.synthetic.main.fragment_cryptocurrency_details.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CryptocurrencyFragment : Fragment() {

    private lateinit var binding:FragmentCryptocurrencyDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCryptocurrencyDetailsBinding.inflate(LayoutInflater.from(context))
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        obverseData()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedCryptoId=arguments?.getString("cryptocurrency_id",null)
        GlobalScope.launch {
            (activity as? MainActivity)?.cryptocurrencyViewModel?.getCountryByName(
                selectedCryptoId ?: return@launch
            )
        }
    }

    private fun obverseData(){
        GlobalScope.launch {
            (activity as? MainActivity)?.cryptocurrencyViewModel?.selectedCryptocurrency?.collect {
                binding.cryptocurrency = it  ?: return@collect
                binding.price="${it?.currentPrice} USD"
                var a=(binding.cryptocurrency?.market_cap_change_percentage_24h ?: 0);
                var b=(binding.cryptocurrency?.price_change_percentage_24h ?: 0);

                if(a.toInt() >0) {
                    binding.tvMarketCapChangePercentage24h.setTextColor(Color.parseColor("#7FFFD4"))
                }else{
                    binding.tvMarketCapChangePercentage24h.setTextColor(Color.parseColor("#DC143C"))
                }
                if(b.toInt() >0) {
                    binding.tvPriceChangePercentage24h.setTextColor(Color.parseColor("#7FFFD4"))
                }else{
                    binding.tvPriceChangePercentage24h.setTextColor(Color.parseColor("#DC143C"))
                }
                (activity as? MainActivity)?.runOnUiThread {
                    setDataBinding()

                    Glide
                        .with(context ?: return@runOnUiThread)
                        .load(it?.image)
                        .centerCrop()
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(binding.ivLogo)
                }


            }
        }
    }
    private fun setDataBinding() {
        binding.cryptocurrency ?: return
        if (binding.cryptocurrency?.favourite == true) {
            binding.btnLike.setImageResource(android.R.drawable.star_big_on)
        } else {
            binding.btnLike.setImageResource(android.R.drawable.star_big_off)
        }

        binding.btnLike.setOnClickListener {
            val cryptocurrency = binding.cryptocurrency
            cryptocurrency?.favourite = cryptocurrency?.favourite != true

            if (cryptocurrency?.favourite == true) {
                binding.btnLike.setImageResource(android.R.drawable.star_big_on)
            } else {
                binding.btnLike.setImageResource(android.R.drawable.star_big_off)
            }

            GlobalScope.launch {
                (activity as? MainActivity)?.cryptocurrencyViewModel?.updateFavourites(
                    cryptocurrency ?: return@launch
                )
            }
        }
    }
}