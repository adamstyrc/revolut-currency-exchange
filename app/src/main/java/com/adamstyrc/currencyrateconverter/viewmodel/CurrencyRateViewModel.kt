package com.adamstyrc.currencyrateconverter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamstyrc.currencyrateconverter.model.CurrencyRate
import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.adamstyrc.currencyrateconverter.model.SupportedCurrency
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class CurrencyRateViewModel @Inject constructor(
    val api: RevolutApi
) : ViewModel() {

    val currencyRateLiveData = MutableLiveData<ArrayList<CurrencyRate>>()
        .apply {
            value = ArrayList<CurrencyRate>()
                .apply {
                    add(
                        CurrencyRate()
                        .apply { currency = "GBR"; rate = 1.10 })
                }
                .apply {
                    add(
                        CurrencyRate()
                        .apply { currency = "USD"; rate = 0.90 })
                }
        }

    fun updateCurrencyRates() {
        api.get(SupportedCurrency.EUR.name)
            .subscribeBy(onSuccess = {
                val value = currencyRateLiveData.value
                value?.add(value[0])
                currencyRateLiveData.postValue(value)

            }, onError = {})
    }
}
