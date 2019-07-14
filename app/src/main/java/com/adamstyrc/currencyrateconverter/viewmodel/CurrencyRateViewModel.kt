package com.adamstyrc.currencyrateconverter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.adamstyrc.currencyrateconverter.model.CalculatedCurrency
import com.adamstyrc.currencyrateconverter.model.Currency
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyRateViewModel @Inject constructor(
    val api: RevolutApi
) : ViewModel() {

    val baseCurrency = MutableLiveData<Currency>()
        .apply { value = Currency.EUR }

    val exchangedCurrencies = MutableLiveData<List<CalculatedCurrency>>()
        .apply {
            value = ArrayList<CalculatedCurrency>()
                .apply {
                    add(
                        CalculatedCurrency(Currency.GBP, 1.10f)
                    )
                }
                .apply {
                    add(
                        CalculatedCurrency(Currency.USD, 0.90f)
                    )
                }
        }

    private var disposable: Disposable? = null

    fun startUpdatingCurrencyRates() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
            .flatMap { api.get(Currency.EUR.name).toObservable() }
            .subscribeBy(onNext = { currencyRateData ->
                val recalculatedExchangedCurrencies = Currency.values().asList()
                    .map { currency ->
                        if (currency == baseCurrency.value) {
                            return@map CalculatedCurrency(currency, 100f)
                        }
                        val currencyName = currency.name
                        val currencyRate = currencyRateData.rates!!.getValue(currencyName)
                        CalculatedCurrency(currency, currencyRate * 100f)
                    }

                exchangedCurrencies.postValue(recalculatedExchangedCurrencies)
            }, onError = {})
    }

    fun cancelUpdatingCurrencyRates() {
        disposable?.dispose()
        disposable = null
    }
}
