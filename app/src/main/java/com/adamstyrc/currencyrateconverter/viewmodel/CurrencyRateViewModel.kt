package com.adamstyrc.currencyrateconverter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamstyrc.currencyrateconverter.model.CalculatedCurrency
import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.adamstyrc.currencyrateconverter.model.Currency
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class CurrencyRateViewModel @Inject constructor(
    val api: RevolutApi
) : ViewModel() {

    val currencyRateLiveData = MutableLiveData<ArrayList<CalculatedCurrency>>()
        .apply {
            value = ArrayList<CalculatedCurrency>()
                .apply {
                    add(
                        CalculatedCurrency(Currency.GBP, 1.10)
                    )
                }
                .apply {
                    add(
                        CalculatedCurrency(Currency.USD, 0.90)
                    )
                }
        }

    fun updateCurrencyRates() {
        api.get(Currency.EUR.name)
            .subscribeBy(onSuccess = {
                val value = currencyRateLiveData.value
                value?.add(value[0])
                currencyRateLiveData.postValue(value)

            }, onError = {})
    }
}
