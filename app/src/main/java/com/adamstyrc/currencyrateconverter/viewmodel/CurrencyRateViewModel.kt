package com.adamstyrc.currencyrateconverter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.adamstyrc.currencyrateconverter.api.model.response.CurrencyRateResponse
import com.adamstyrc.currencyrateconverter.model.CalculatedCurrency
import com.adamstyrc.currencyrateconverter.model.Currency
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyRateViewModel @Inject constructor(
    private val api: RevolutApi
) : ViewModel() {

    val exchangedCurrencies = MutableLiveData<ArrayList<CalculatedCurrency>>()
        .apply { value = ArrayList() }
    var baseCurrency: Currency
        get() = orderedCurrencies[0]
        set(currency) {
            onBaseCurrencyChanged(currency)
        }

    private val orderedCurrencies = arrayListOf(
        Currency.EUR,
        Currency.USD,
        Currency.GBP,
        Currency.AUD,
        Currency.PLN,
        Currency.JPY,
        Currency.CZK
    )
    private var baseCurrencyAmount = 100f
    private var latestCurrencyRates: CurrencyRateResponse? = null
    private var disposable: Disposable? = null

    fun startUpdatingCurrencyRates() {
        disposable = Observable.interval(1, TimeUnit.SECONDS)
            .flatMap { api.get(baseCurrency.name).toObservable() }
            .subscribeBy(onNext = { currencyRateData ->
                latestCurrencyRates = currencyRateData
                updateExchangedCurrencies()
            }, onError = {})
    }

    fun cancelUpdatingCurrencyRates() {
        disposable?.dispose()
        disposable = null
    }

    fun setBaseCurrencyAmount(amount: Float) {
        baseCurrencyAmount = amount
        updateExchangedCurrencies()
    }

    private fun updateExchangedCurrencies() {
        val currencyRates = latestCurrencyRates
        if (currencyRates == null || currencyRates.base != baseCurrency.name) {
            return
        }

        val latestExchangedByBaseCurrencies = orderedCurrencies.map { currency ->
            if (currency == orderedCurrencies[0]) {
                return@map CalculatedCurrency(currency, baseCurrencyAmount)
            }
            val currencyName = currency.name
            val currencyRate = currencyRates.rates?.get(currencyName)
            if (currencyRate != null) {
                return@map CalculatedCurrency(currency, currencyRate * baseCurrencyAmount)
            } else {
                return
            }
        }

        exchangedCurrencies.postValue(ArrayList(latestExchangedByBaseCurrencies))
    }

    private fun onBaseCurrencyChanged(currency: Currency) {
        cancelUpdatingCurrencyRates()
        orderedCurrencies.remove(currency)
        orderedCurrencies.add(0, currency)

        baseCurrencyAmount = exchangedCurrencies.value
            ?.find { it.currency == currency }
            ?.value!!

        startUpdatingCurrencyRates()
    }
}
