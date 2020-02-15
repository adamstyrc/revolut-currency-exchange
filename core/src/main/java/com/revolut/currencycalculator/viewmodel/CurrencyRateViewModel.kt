package com.revolut.currencycalculator.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.revolut.currencycalculator.api.RevolutApi
import com.revolut.currencycalculator.utils.Logger
import com.revolut.domain.Price
import com.revolut.domain.calculator.CurrencyExchangeCalculator
import com.revolut.domain.model.Currency
import com.revolut.domain.model.CurrencyValuation
import com.revolut.domain.model.CalculatedCurrencyPrice
import com.revolut.domain.transformation.CurrencyValuationTransformations
import com.revolut.ports.LocalCurrencyValuationRepository
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyRateViewModel @Inject constructor(
    private val api: RevolutApi,
    private val localCurrencyValuationRepository: LocalCurrencyValuationRepository
) : ViewModel() {

    companion object {
        const val AUTO_REFRESH_PERIOD_IN_SECONDS = 1L
        const val DEFAULT_BASE_CURRENCY_AMOUNT = 100.0
    }

    private var demandedCurrencies: MutableList<Currency> = ArrayList(Currency.values().toList())
    private var baseCurrencyAmount = BigDecimal(DEFAULT_BASE_CURRENCY_AMOUNT)
    private var currencyExchangeCalculator = CurrencyExchangeCalculator()
    private val currencyValuationLiveData =
        localCurrencyValuationRepository.getCurrencyValuation()
    private val currencyValuationObserver = Observer<CurrencyValuation?> { currencyValuation ->
        if (currencyValuation != null) {
            recalculateCurrenciesPrices(currencyValuation)
        }
    }
    private val calculatedCurrenciesPricesList = MutableLiveData<List<CalculatedCurrencyPrice>>()
    private var disposable = Disposables.disposed()

    init {
        currencyValuationLiveData.observeForever(currencyValuationObserver)
    }

    override fun onCleared() {
        super.onCleared()

        disposable.dispose()
        currencyValuationLiveData.removeObserver(currencyValuationObserver)
    }


    fun setDemandedCurrencies(currencies: List<Currency>) {
        demandedCurrencies = ArrayList(currencies)
    }

    fun startUpdatingCurrencyRates() {
        // TODO consider rewriting so the first request is fired instantly and others every second
        disposable = Observable.interval(AUTO_REFRESH_PERIOD_IN_SECONDS, TimeUnit.SECONDS)
            .flatMapMaybe {
                api.getLatest(getBaseCurrency().name)
                    .filter { it.isValid() }
                    .map { it.toDomainModel() }
            }
            .retry()
            .subscribeBy(
                onNext = { currencyRateData ->
                    localCurrencyValuationRepository.saveCurrencyValuation(currencyRateData)
                },
                onError = {
                    it.printStackTrace()
                })
    }

    fun cancelUpdatingCurrencyRates() {
        disposable.dispose()
    }

    fun setBaseCurrencyAmount(amount: Price) {
        baseCurrencyAmount = amount
        currencyValuationLiveData.value?.let { currencyValuation ->
            recalculateCurrenciesPrices(currencyValuation)
        }
    }

    fun setBaseCurrency(currency: Currency) {
        cancelUpdatingCurrencyRates()
        demandedCurrencies.remove(currency)
        demandedCurrencies.add(0, currency)

        calculatedCurrenciesPricesList.value?.let { calculatedCurrenciesPrices ->
            baseCurrencyAmount =
                calculatedCurrenciesPrices.find { it.currency == currency }!!.value
        }

        startUpdatingCurrencyRates()
    }

    fun getCalculatedCurrencyExchange() =
        calculatedCurrenciesPricesList

    fun saveState(outState: Bundle) {
        outState.putStringArray(
            SavedItems.DEMANDED_CURRENCIES.name,
            demandedCurrencies.map { it.name }.toTypedArray()
        )

        outState.putDouble(SavedItems.BASE_CURRENCY_AMOUNT.name, baseCurrencyAmount.toDouble())
    }

    fun restoreState(savedState: Bundle) {
        Logger.log("CurrencyRateView: restoreState($savedState)")

        savedState.getStringArray(SavedItems.DEMANDED_CURRENCIES.name)
            ?.let { restoredDemandedCurrencies ->
                this.demandedCurrencies = restoredDemandedCurrencies.toList()
                    .map { Currency.valueOf(it) }
                    .toMutableList()
            }

        savedState.getDouble(SavedItems.BASE_CURRENCY_AMOUNT.name, DEFAULT_BASE_CURRENCY_AMOUNT)
            .let { restoredBaseCurrencyAmount ->
                this.baseCurrencyAmount = Price.valueOf(restoredBaseCurrencyAmount)
            }

        currencyValuationLiveData.value?.let { currencyValuation ->
            recalculateCurrenciesPrices(currencyValuation)
        }

    }

    private fun recalculateCurrenciesPrices(currencyValuation: CurrencyValuation) {
        val recalculatedCurrenciesPrices = calculateCurrenciesPrices(
            getBaseCurrency(),
            baseCurrencyAmount,
            currencyValuation
        )

        calculatedCurrenciesPricesList.postValue(recalculatedCurrenciesPrices)
    }

    private fun getBaseCurrency() = demandedCurrencies[0]

    private fun calculateCurrenciesPrices(
        soldCurrency: Currency,
        soldCurrencyAmount: Price,
        latestCurrencyValuation: CurrencyValuation?
    ): List<CalculatedCurrencyPrice> {
        if (latestCurrencyValuation == null) {
            return emptyList()
        }

        val currencyValuation: CurrencyValuation =
            (if (soldCurrency == latestCurrencyValuation.base)
                latestCurrencyValuation
            else
                CurrencyValuationTransformations.transformCurrencyValuationForNewCurrency(
                    latestCurrencyValuation,
                    soldCurrency
                ))
                ?: return emptyList()

        return demandedCurrencies.map { currency ->
            val calculatedPrice =
                currencyExchangeCalculator.calculateBoughtCurrencyValue(
                    soldCurrency,
                    soldCurrencyAmount,
                    currency,
                    currencyValuation
                )

            CalculatedCurrencyPrice(
                currency = currency,
                value = calculatedPrice ?: Price.valueOf(0)
            )
        }
    }

    enum class SavedItems {
        DEMANDED_CURRENCIES,
        BASE_CURRENCY_AMOUNT;
    }
}
