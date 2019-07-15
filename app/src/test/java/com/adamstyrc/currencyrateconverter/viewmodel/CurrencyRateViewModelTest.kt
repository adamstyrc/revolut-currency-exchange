package com.adamstyrc.currencyrateconverter.viewmodel

import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.adamstyrc.currencyrateconverter.api.model.response.CurrencyRateResponse
import com.adamstyrc.currencyrateconverter.model.Currency
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Observable
import org.junit.Rule

// TODO testing of viewmodel
// mock API with Dagger
class CurrencyRateViewModelTest {

    @Rule @JvmField
    val rule : InstantTaskExecutorRule = InstantTaskExecutorRule()

    private var api = mock(RevolutApi::class.java)
    private lateinit var viewModel: CurrencyRateViewModel

    @Before
    fun init() {
        `when`(api.get(Currency.EUR.name))
            .thenReturn(Observable.just(currencyRateResponseForEUR))
        `when`(api.get(Currency.USD.name))
            .thenReturn(Observable.just(currencyRateResponseForEUR))

        viewModel = CurrencyRateViewModel(api)
        viewModel.orderedCurrencies = orderedCurrencies
        viewModel.latestCurrencyRates = currencyRateResponseForEUR
        viewModel.updateExchangedCurrencies()
    }

    @Test
    fun `base currency is set to EUR`() {
        viewModel.estimatedCurrenciesExchange.observeForever { estimatedCurrenciesExchange ->
            assertEquals(3, estimatedCurrenciesExchange?.size)
            assertEquals(Currency.EUR, estimatedCurrenciesExchange?.get(0)?.currency)
        }
    }

    @Test
    fun `change base currency from EUR to USD with new currency rates`() {
        viewModel.setBaseCurrency(Currency.USD)
        viewModel.latestCurrencyRates = currencyRateResponseForUSD
        viewModel.updateExchangedCurrencies()

        viewModel.estimatedCurrenciesExchange.observeForever { estimatedCurrenciesExchange ->
            assertEquals(3, estimatedCurrenciesExchange?.size)
            assertEquals(Currency.USD, estimatedCurrenciesExchange?.get(0)?.currency)
            assertEquals(Currency.EUR, estimatedCurrenciesExchange?.get(1)?.currency)
            assertEquals(Currency.PLN, estimatedCurrenciesExchange?.get(2)?.currency)
        }
    }

//    @Test
//    fun `change base currency from EUR to PLN with no currency rates`() {
//        viewModel.setBaseCurrency(Currency.PLN)
//        viewModel.latestCurrencyRates = currencyRateResponseForUSD
//        viewModel.updateExchangedCurrencies()
//    }

    private val orderedCurrencies = arrayListOf(
        Currency.EUR,
        Currency.USD,
        Currency.PLN
    )
    private val currencyRateResponseForEUR = CurrencyRateResponse()
        .apply { base = "EUR"; rates = hashMapOf(
            Pair("USD", 1.1652f),
            Pair("PLN", 4.3248f)
        )}
    private val currencyRateResponseForUSD = CurrencyRateResponse()
        .apply { base = "USD"; rates = hashMapOf(
            Pair("EUR", 0.85646f),
            Pair("PLN", 3.6984f)
        )}
}
