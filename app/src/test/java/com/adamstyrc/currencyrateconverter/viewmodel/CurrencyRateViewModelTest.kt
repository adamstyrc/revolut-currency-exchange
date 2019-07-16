package com.adamstyrc.currencyrateconverter.viewmodel

import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.adamstyrc.currencyrateconverter.api.model.response.CurrencyRateResponse
import com.adamstyrc.currencyrateconverter.model.Currency
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

    // This rule is forced by LiveData trying to call MainThread
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
        val expectedNewBaseAmount = 100f * 1.1652f

        viewModel.setBaseCurrency(Currency.USD)
        viewModel.latestCurrencyRates = currencyRateResponseForUSD
        viewModel.updateExchangedCurrencies()

        viewModel.estimatedCurrenciesExchange.observeForever { estimatedCurrenciesExchange ->
            assertEquals(3, estimatedCurrenciesExchange?.size)

            assertEquals(Currency.USD, estimatedCurrenciesExchange?.get(0)?.currency)
            assertEquals(expectedNewBaseAmount, estimatedCurrenciesExchange?.get(0)?.value)

            assertEquals(Currency.EUR, estimatedCurrenciesExchange?.get(1)?.currency)
            assertEquals(expectedNewBaseAmount * 0.85646f, estimatedCurrenciesExchange?.get(1)?.value)

            assertEquals(Currency.PLN, estimatedCurrenciesExchange?.get(2)?.currency)
            assertEquals(expectedNewBaseAmount * 3.6984f, estimatedCurrenciesExchange?.get(2)?.value)
        }
    }

    @Test
    fun `change base currency from EUR to PLN with no new currency rates`() {
        val expectedNewBaseAmount = 100f * 4.3248f

        viewModel.setBaseCurrency(Currency.PLN)
        viewModel.updateExchangedCurrencies()

        viewModel.estimatedCurrenciesExchange.observeForever { estimatedCurrenciesExchange ->
            assertEquals(3, estimatedCurrenciesExchange?.size)

            assertEquals(Currency.PLN, estimatedCurrenciesExchange?.get(0)?.currency)
            assertEquals(expectedNewBaseAmount, estimatedCurrenciesExchange?.get(0)?.value)

            assertEquals(Currency.EUR, estimatedCurrenciesExchange?.get(1)?.currency)
            assertEquals(100f, estimatedCurrenciesExchange?.get(1)?.value)

            assertEquals(Currency.USD, estimatedCurrenciesExchange?.get(2)?.currency)
            assertEquals(116.52f, estimatedCurrenciesExchange?.get(2)?.value)
        }
    }

    @Test
    fun `set amount to 0`() {
        viewModel.setBaseCurrencyAmount(0f)
        viewModel.updateExchangedCurrencies()

        viewModel.estimatedCurrenciesExchange.observeForever { estimatedCurrenciesExchange ->
            assertEquals(3, estimatedCurrenciesExchange?.size)

            assertEquals(Currency.EUR, estimatedCurrenciesExchange?.get(0)?.currency)
            assertEquals(0f, estimatedCurrenciesExchange?.get(0)?.value)

            assertEquals(Currency.USD, estimatedCurrenciesExchange?.get(1)?.currency)
            assertEquals(0f, estimatedCurrenciesExchange?.get(1)?.value)

            assertEquals(Currency.PLN, estimatedCurrenciesExchange?.get(2)?.currency)
            assertEquals(0f, estimatedCurrenciesExchange?.get(2)?.value)
        }
    }

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
