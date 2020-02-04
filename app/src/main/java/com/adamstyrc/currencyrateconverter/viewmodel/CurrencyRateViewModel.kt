package com.adamstyrc.currencyrateconverter.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.revolut.domain.Money
import com.revolut.domain.calculator.CurrencyExchangeCalculator
import com.revolut.domain.model.Currency
import com.revolut.domain.model.CurrencyValuation
import com.revolut.domain.model.EstimatedCurrencyExchange
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
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

    private val estimatedCurrenciesExchange =
        MutableLiveData<MutableList<EstimatedCurrencyExchange>>(arrayListOf())
    private var demandedCurrencies: MutableList<Currency> = ArrayList(Currency.values().toList())
    private var latestCurrencyValuation: CurrencyValuation? = null
    private var baseCurrencyAmount = BigDecimal(100.00)
    private var currencyExchangeCalculator = CurrencyExchangeCalculator()
    private var disposable = Disposables.disposed()

    override fun onCleared() {
        super.onCleared()

        disposable.dispose()
    }

    fun getEstimatedCurrencyExchange(): LiveData<MutableList<EstimatedCurrencyExchange>> =
        estimatedCurrenciesExchange

    fun setDemandedCurrencies(currencies: List<Currency>) {
        demandedCurrencies = ArrayList(currencies)
    }

    fun startUpdatingCurrencyRates() {
        // TODO consider rewriting so the first request is fired instantly and others every second
        disposable = Observable.interval(AUTO_REFRESH_PERIOD_IN_SECONDS, TimeUnit.SECONDS)
            .flatMap {
                api.getLatest(getBaseCurrency().name)
                    .filter { it.isValid() }
                    .map { it.toDomainModel() }
                    .toObservable()
            }
            .retry()
            .subscribeBy(onNext = { currencyRateData ->
                latestCurrencyValuation = currencyRateData
                updateExchangedCurrencies()
            }, onError = {})
    }

    fun cancelUpdatingCurrencyRates() {
        disposable.dispose()
    }

    fun setBaseCurrencyAmount(amount: Money) {
        baseCurrencyAmount = amount
        updateExchangedCurrencies()
    }

    fun setBaseCurrency(currency: Currency) {
        cancelUpdatingCurrencyRates()
        demandedCurrencies.remove(currency)
        demandedCurrencies.add(0, currency)

        baseCurrencyAmount = estimatedCurrenciesExchange.value
            ?.find { it.currency == currency }
            ?.value!!

        startUpdatingCurrencyRates()
    }

    fun updateExchangedCurrencies() {
        val currencyRates = latestCurrencyValuation
        if (currencyRates == null
            || currencyRates.base != getBaseCurrency()) {
            orderEstimatedCurrenciesExchange()
            return
        }

        val latestExchangedByBaseCurrencies =
            demandedCurrencies.map { currency ->
                if (currency == demandedCurrencies[0]) {
                    return@map EstimatedCurrencyExchange(currency, baseCurrencyAmount)
                }

                currencyExchangeCalculator.calculate(
                    currencyRates.rates,
                    currency,
                    baseCurrencyAmount
                )?.let { estimatedCurrenciesExchange ->
                    return@map estimatedCurrenciesExchange
                }

                return
            }

        estimatedCurrenciesExchange.postValue(ArrayList(latestExchangedByBaseCurrencies))
    }

    @VisibleForTesting
    fun setLatestCurrencyValuation(currencyValuation: CurrencyValuation?) {
        latestCurrencyValuation = currencyValuation
    }

    private fun orderEstimatedCurrenciesExchange() {
        val estimatedCurrenciesExchangeList =
            estimatedCurrenciesExchange.value
        if (estimatedCurrenciesExchangeList != null
            && estimatedCurrenciesExchangeList.isNotEmpty()
        ) {
            val orderedEstimatedCurrenciesExchangeList =
                demandedCurrencies.map { currency ->
                    estimatedCurrenciesExchangeList.find { it.currency == currency }!!
                }
            estimatedCurrenciesExchange.postValue(ArrayList(orderedEstimatedCurrenciesExchangeList))
        }
    }

    private fun getBaseCurrency() = demandedCurrencies[0]

}
