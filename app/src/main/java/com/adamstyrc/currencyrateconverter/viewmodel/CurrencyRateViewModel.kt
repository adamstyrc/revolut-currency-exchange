package com.adamstyrc.currencyrateconverter.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.adamstyrc.currencyrateconverter.api.model.response.CurrencyRateResponse
import com.adamstyrc.currencyrateconverter.model.Currency
import com.adamstyrc.currencyrateconverter.model.EstimatedCurrencyExchange
import com.adamstyrc.currencyrateconverter.model.Money
import com.adamstyrc.currencyrateconverter.util.CurrencyExchangeCalculator
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyRateViewModel @Inject constructor(
    private val api: RevolutApi
) : ViewModel() {

    companion object {
        const val AUTO_REFRESH_PERIOD_IN_SECONDS = 1L
    }

    val estimatedCurrenciesExchange = MutableLiveData<ArrayList<EstimatedCurrencyExchange>>()
        .apply { value = ArrayList() }
    var orderedCurrencies = arrayListOf<Currency>()
    internal var latestCurrencyRates: CurrencyRateResponse? = null
    private var baseCurrencyAmount = BigDecimal(100)
    private var currencyExchangeCalculator = CurrencyExchangeCalculator()
    private var disposable: Disposable? = null

    fun startUpdatingCurrencyRates() {
        // TODO consider rewriting so the first request is fired instantly and others every second
        disposable = Observable.interval(AUTO_REFRESH_PERIOD_IN_SECONDS, TimeUnit.SECONDS)
            .flatMap { api.get(getBaseCurrency().name) }
            .subscribeBy(onNext = { currencyRateData ->
                latestCurrencyRates = currencyRateData
                updateExchangedCurrencies()
            }, onError = {})
    }

    fun cancelUpdatingCurrencyRates() {
        disposable?.dispose()
        disposable = null
    }

    fun setBaseCurrencyAmount(amount: Money) {
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

    internal fun updateExchangedCurrencies() {
        val currencyRates = latestCurrencyRates
        if (currencyRates?.rates == null || currencyRates.base != getBaseCurrency().name) {
            orderEstimatedCurrenciesExchange()
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

    private fun orderEstimatedCurrenciesExchange() {
        val estimatedCurrenciesExchangeList = estimatedCurrenciesExchange.value
        if (estimatedCurrenciesExchangeList != null && estimatedCurrenciesExchangeList.isNotEmpty()) {
            val orderedEstimatedCurrenciesExchangeList = orderedCurrencies.map { currency ->
                estimatedCurrenciesExchangeList.find { it.currency == currency }!!
            }
            estimatedCurrenciesExchange.postValue(ArrayList(orderedEstimatedCurrenciesExchangeList))
        }
    }

    private fun getBaseCurrency()  = orderedCurrencies[0]

}
