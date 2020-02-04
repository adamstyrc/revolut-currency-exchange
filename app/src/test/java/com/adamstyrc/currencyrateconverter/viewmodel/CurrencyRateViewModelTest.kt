package com.adamstyrc.currencyrateconverter.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.adamstyrc.currencyrateconverter.api.RevolutApi
import com.adamstyrc.currencyrateconverter.api.model.response.CurrencyRateResponse
import com.revolut.domain.model.Currency
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.math.BigDecimal

class CurrencyRateViewModelTest {

    // This rule is forced by LiveData trying to call MainThread
    @Rule
    @JvmField
    val rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private var api = mock(RevolutApi::class.java)
    private lateinit var viewModel: CurrencyRateViewModel

    @Before
    fun init() {
        `when`(api.getLatest(Currency.EUR.name))
            .thenReturn(Observable.just(currencyRateResponseForEUR))
        `when`(api.getLatest(Currency.USD.name))
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
            assertTrue(BigDecimal.valueOf(100)
                .compareTo(estimatedCurrenciesExchange?.get(0)?.value) == 0)

            assertEquals(Currency.USD, estimatedCurrenciesExchange?.get(1)?.currency)
            assertTrue(BigDecimal.valueOf(116.52)
                .compareTo(estimatedCurrenciesExchange?.get(1)?.value) == 0)

            assertEquals(Currency.PLN, estimatedCurrenciesExchange?.get(2)?.currency)
            assertTrue(BigDecimal.valueOf(432.48)
                .compareTo(estimatedCurrenciesExchange?.get(2)?.value) == 0)
        }
    }

    @Test
    fun `change base currency from EUR to USD with new currency rates`() {
        val expectedNewBaseAmount = BigDecimal.valueOf(116.52)

        viewModel.setBaseCurrency(Currency.USD)
        viewModel.latestCurrencyRates = currencyRateResponseForUSD
        viewModel.updateExchangedCurrencies()

        viewModel.estimatedCurrenciesExchange.observeForever { estimatedCurrenciesExchange ->
            assertEquals(3, estimatedCurrenciesExchange?.size)

            assertEquals(Currency.USD, estimatedCurrenciesExchange?.get(0)?.currency)
            assertTrue(expectedNewBaseAmount
                .compareTo(estimatedCurrenciesExchange?.get(0)?.value) == 0)

            assertEquals(Currency.EUR, estimatedCurrenciesExchange?.get(1)?.currency)
            assertTrue(expectedNewBaseAmount.multiply(BigDecimal.valueOf(0.85646))
                .compareTo(estimatedCurrenciesExchange?.get(1)?.value) == 0)

            assertEquals(Currency.PLN, estimatedCurrenciesExchange?.get(2)?.currency)
            assertTrue(expectedNewBaseAmount.multiply(BigDecimal.valueOf(3.6984))
                .compareTo(estimatedCurrenciesExchange?.get(2)?.value) == 0)
        }
    }

    @Test
    fun `change base currency from EUR to PLN with no new currency rates`() {
        val expectedNewBaseAmount = BigDecimal.valueOf(432.48)

        viewModel.setBaseCurrency(Currency.PLN)
        viewModel.updateExchangedCurrencies()

        viewModel.estimatedCurrenciesExchange.observeForever { estimatedCurrenciesExchange ->
            assertEquals(3, estimatedCurrenciesExchange?.size)

            assertEquals(Currency.PLN, estimatedCurrenciesExchange?.get(0)?.currency)
            assertTrue(expectedNewBaseAmount
                .compareTo(estimatedCurrenciesExchange?.get(0)?.value) == 0)

            assertEquals(Currency.EUR, estimatedCurrenciesExchange?.get(1)?.currency)
            assertTrue(BigDecimal.valueOf(100)
                .compareTo(estimatedCurrenciesExchange?.get(1)?.value) == 0)

            assertEquals(Currency.USD, estimatedCurrenciesExchange?.get(2)?.currency)
            assertTrue(BigDecimal.valueOf(116.52)
                .compareTo(estimatedCurrenciesExchange?.get(2)?.value) == 0)
        }
    }

    @Test
    fun `set amount to 0`() {
        viewModel.setBaseCurrencyAmount(BigDecimal.ZERO)
        viewModel.updateExchangedCurrencies()

        viewModel.estimatedCurrenciesExchange.observeForever { estimatedCurrenciesExchange ->
            assertEquals(3, estimatedCurrenciesExchange?.size)

            assertEquals(Currency.EUR, estimatedCurrenciesExchange?.get(0)?.currency)
            assertTrue(BigDecimal.ZERO
                .compareTo(estimatedCurrenciesExchange?.get(1)?.value) == 0)

            assertEquals(Currency.USD, estimatedCurrenciesExchange?.get(1)?.currency)
            assertTrue(BigDecimal.ZERO
                .compareTo(estimatedCurrenciesExchange?.get(1)?.value) == 0)

            assertEquals(Currency.PLN, estimatedCurrenciesExchange?.get(2)?.currency)
            assertTrue(BigDecimal.ZERO
                .compareTo(estimatedCurrenciesExchange?.get(2)?.value) == 0)
        }
    }

    @Test
    fun `set amount to 999,99`() {
        viewModel.setBaseCurrencyAmount(BigDecimal.valueOf(999.99))
        viewModel.updateExchangedCurrencies()

        viewModel.estimatedCurrenciesExchange.observeForever { estimatedCurrenciesExchange ->
            assertEquals(3, estimatedCurrenciesExchange?.size)

            assertEquals(Currency.EUR, estimatedCurrenciesExchange?.get(0)?.currency)
            assertTrue(BigDecimal.valueOf(999.99)
                .compareTo(estimatedCurrenciesExchange?.get(0)?.value) == 0)

            assertEquals(Currency.USD, estimatedCurrenciesExchange?.get(1)?.currency)
            assertTrue(BigDecimal.valueOf(1165.188348)
                .compareTo(estimatedCurrenciesExchange?.get(1)?.value) == 0)

            assertEquals(Currency.PLN, estimatedCurrenciesExchange?.get(2)?.currency)
            assertTrue(BigDecimal.valueOf(4324.756752)
                .compareTo(estimatedCurrenciesExchange?.get(2)?.value) == 0)
        }
    }

    private val orderedCurrencies = arrayListOf(
        Currency.EUR,
        Currency.USD,
        Currency.PLN
    )
    private val currencyRateResponseForEUR = CurrencyRateResponse()
        .apply {
            base = "EUR"; rates = hashMapOf(
            Pair("USD", BigDecimal.valueOf(1.1652)),
            Pair("PLN", BigDecimal.valueOf(4.3248))
        )
        }
    private val currencyRateResponseForUSD = CurrencyRateResponse()
        .apply {
            base = "USD"; rates = hashMapOf(
            Pair("EUR", BigDecimal.valueOf(0.85646)),
            Pair("PLN", BigDecimal.valueOf(3.6984))
        )
        }
}
