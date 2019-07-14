package com.adamstyrc.currencyrateconverter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.adamstyrc.currencyrateconverter.api.model.response.CurrencyRateResponse
import com.adamstyrc.currencyrateconverter.model.EstimatedCurrencyExchange
import com.adamstyrc.currencyrateconverter.model.Currency
import com.adamstyrc.currencyrateconverter.util.CurrencyExchangeCalculator
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyRateViewModel @Inject constructor(
    private val api: RevolutApi
) : ViewModel() {

    companion object {
        val AUTO_REFRESH_PERIOD = TimeUnit.SECONDS
    }

    val estimatedCurrenciesExchange = MutableLiveData<ArrayList<EstimatedCurrencyExchange>>()
        .apply { value = ArrayList() }

    private val orderedCurrencies = arrayListOf(
        Currency.EUR,
        Currency.USD,
        Currency.GBP,
        Currency.AUD,
        Currency.PLN,
        Currency.JPY,
        Currency.CZK
    )
    private var currencyExchangeCalculator = CurrencyExchangeCalculator()
    private var baseCurrencyAmount = 100f
    private var latestCurrencyRates: CurrencyRateResponse? = null
    private var disposable: Disposable? = null

    fun startUpdatingCurrencyRates() {
        disposable = Observable.interval(1, AUTO_REFRESH_PERIOD)
            .flatMap { api.get(getBaseCurrency().name).toObservable() }
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

    fun setBaseCurrency(currency: Currency) {
        cancelUpdatingCurrencyRates()
        orderedCurrencies.remove(currency)
        orderedCurrencies.add(0, currency)

        baseCurrencyAmount = estimatedCurrenciesExchange.value
            ?.find { it.currency == currency }
            ?.value!!

        startUpdatingCurrencyRates()
    }

    private fun updateExchangedCurrencies() {
        val currencyRates = latestCurrencyRates
        if (currencyRates?.rates == null || currencyRates.base != getBaseCurrency().name) {
            return
        }

        val latestExchangedByBaseCurrencies = orderedCurrencies.map { currency ->
            if (currency == orderedCurrencies[0]) {
                return@map EstimatedCurrencyExchange(currency, baseCurrencyAmount)
            }

            currencyExchangeCalculator.calculate(
                currencyRates.rates!!,
                currency,
                baseCurrencyAmount)?.let { estimatedCurrenciesExchange ->
                return@map estimatedCurrenciesExchange
            }

            return
        }

        estimatedCurrenciesExchange.postValue(ArrayList(latestExchangedByBaseCurrencies))
    }

    private fun getBaseCurrency()  = orderedCurrencies[0]

}
