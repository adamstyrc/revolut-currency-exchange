package com.adamstyrc.currencyrateconverter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamstyrc.currencyrateconverter.CurrencyRate

class CurrencyRateViewModel : ViewModel() {

    val currencyRateLiveData = MutableLiveData<List<CurrencyRate>>()
        .apply {
            value = ArrayList<CurrencyRate>()
                .apply {
                    add(CurrencyRate()
                        .apply { currency = "GBR"; rate = 1.10 })
                }
                .apply {
                    add(CurrencyRate()
                        .apply { currency = "USD"; rate = 0.90 })
                }
        }
}
