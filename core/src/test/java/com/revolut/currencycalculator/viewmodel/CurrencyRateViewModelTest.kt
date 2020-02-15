package com.revolut.currencycalculator.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.revolut.currencycalculator.InMemoryCurrencyValuationRepository
import com.revolut.currencycalculator.api.RevolutApi
import com.revolut.domain.Price
import com.revolut.domain.model.CalculatedCurrencyPrice
import com.revolut.domain.model.Currency
import com.revolut.testpack.CurrencyValuationTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.math.BigDecimal

class CurrencyRateViewModelTest {

    // This rule is forced by LiveData trying to call MainThread
    @Rule
    @JvmField
    val rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val api = mock(RevolutApi::class.java)
    private val currencyValuationRepository = InMemoryCurrencyValuationRepository()
    private lateinit var viewModel: CurrencyRateViewModel

    private val currencyValuationTestData = CurrencyValuationTestData()

    private val orderedCurrencies = arrayListOf(
        Currency.EUR,
        Currency.USD,
        Currency.PLN
    )

    @Before
    fun init() {
//        `when`(api.getLatest(Currency.EUR.name))
//            .thenReturn(Single.just(valuationEUR))
//        `when`(api.getLatest(Currency.USD.name))
//            .thenReturn(Single.just(valuationEUR))

        viewModel = CurrencyRateViewModel(api, currencyValuationRepository)
        viewModel.setDemandedCurrencies(orderedCurrencies)
    }

    @Test
    fun `base currency is set to EUR`() {
        currencyValuationRepository.saveCurrencyValuation(currencyValuationTestData.ratesForEUR1)

        viewModel.getCalculatedCurrencyExchange().observeForever { estimatedCurrenciesExchange ->
            assertThat(estimatedCurrenciesExchange?.size)
                .isEqualTo(3)

            assertThat(estimatedCurrenciesExchange?.get(0)?.currency)
                .isEqualTo(Currency.EUR)
            assertThat(estimatedCurrenciesExchange?.get(0)?.value)
                .isEqualToIgnoringScale(BigDecimal.valueOf(100))

            assertThat(estimatedCurrenciesExchange?.get(1)?.currency)
                .isEqualTo(Currency.USD)
            assertThat(estimatedCurrenciesExchange?.get(1)?.value)
                .isEqualToIgnoringScale(BigDecimal.valueOf(116.016))

            assertThat(estimatedCurrenciesExchange?.get(2)?.currency)
                .isEqualTo(Currency.PLN)
            assertThat(estimatedCurrenciesExchange?.get(2)?.value)
                .isEqualToIgnoringScale(BigDecimal.valueOf(431.834))

        }
    }

    @Test
    fun `change base currency from EUR to USD with new currency rates`() {
        val expectedNewBaseAmount = BigDecimal.valueOf(116.52)

        viewModel.setBaseCurrency(Currency.USD)

        viewModel.getCalculatedCurrencyExchange().observeForever { estimatedCurrenciesExchange ->
            assertThat(estimatedCurrenciesExchange?.size)
                .isEqualTo(3)

            assertThat(estimatedCurrenciesExchange?.get(0)?.currency)
                .isEqualTo(Currency.USD)
            assertThat(estimatedCurrenciesExchange?.get(0)?.value)
                .isEqualToIgnoringScale(expectedNewBaseAmount)

            assertThat(estimatedCurrenciesExchange?.get(1)?.currency)
                .isEqualTo(Currency.EUR)
            assertThat(estimatedCurrenciesExchange?.get(1)?.value)
                .isEqualToIgnoringScale(expectedNewBaseAmount.multiply(BigDecimal.valueOf(0.85646)))

            assertThat(estimatedCurrenciesExchange?.get(2)?.currency)
                .isEqualTo(Currency.PLN)
            assertThat(estimatedCurrenciesExchange?.get(2)?.value)
                .isEqualToIgnoringScale(expectedNewBaseAmount.multiply(BigDecimal.valueOf(3.6984)))
        }
    }

    @Test
    fun `change base currency from EUR to PLN with no new currency rates`() {
        val expectedNewBaseAmount = BigDecimal.valueOf(432.48)

        viewModel.setBaseCurrency(Currency.PLN)

        viewModel.getCalculatedCurrencyExchange().observeForever { estimatedCurrenciesExchange ->
            assertThat(estimatedCurrenciesExchange?.size)
                .isEqualTo(3)

            assertThat(estimatedCurrenciesExchange?.get(0)?.currency)
                .isEqualTo(Currency.PLN)
            assertThat(estimatedCurrenciesExchange?.get(0)?.value)
                .isEqualToIgnoringScale(expectedNewBaseAmount)

            assertThat(estimatedCurrenciesExchange?.get(1)?.currency)
                .isEqualTo(Currency.EUR)
            assertThat(estimatedCurrenciesExchange?.get(1)?.value)
                .isEqualToIgnoringScale(BigDecimal.valueOf(100))

            assertThat(estimatedCurrenciesExchange?.get(2)?.currency)
                .isEqualTo(Currency.USD)
            assertThat(estimatedCurrenciesExchange?.get(2)?.value)
                .isEqualToIgnoringScale(BigDecimal.valueOf(116.52))
        }
    }

    @Test
    fun `set amount to 0`() {
        viewModel.setBaseCurrencyAmount(BigDecimal.ZERO)

        viewModel.getCalculatedCurrencyExchange().observeForever { estimatedCurrenciesExchange ->
            assertThat(estimatedCurrenciesExchange?.size)
                .isEqualTo(3)

            assertThat(estimatedCurrenciesExchange?.get(0)?.currency)
                .isEqualTo(Currency.EUR)
            assertThat(estimatedCurrenciesExchange?.get(0)?.value)
                .isEqualToIgnoringScale(BigDecimal.ZERO)


            checkCurrencyExchangeIsEqualTo(
                estimatedCurrenciesExchange?.get(1),
                Currency.USD,
                BigDecimal.ZERO)

            checkCurrencyExchangeIsEqualTo(
                estimatedCurrenciesExchange?.get(2),
                Currency.PLN,
                BigDecimal.ZERO)
        }
    }

    @Test
    fun `set amount to 999,99`() {
        viewModel.setBaseCurrencyAmount(BigDecimal.valueOf(999.99))

        viewModel.getCalculatedCurrencyExchange().observeForever { estimatedCurrenciesExchange ->
            assertThat(estimatedCurrenciesExchange?.size)
                .isEqualTo(3)

            checkCurrencyExchangeIsEqualTo(
                estimatedCurrenciesExchange?.get(0),
                Currency.EUR,
                BigDecimal.valueOf(999.99))


            checkCurrencyExchangeIsEqualTo(
                estimatedCurrenciesExchange?.get(1),
                Currency.USD,
                BigDecimal.valueOf(1165.188348))

            checkCurrencyExchangeIsEqualTo(
                estimatedCurrenciesExchange?.get(2),
                Currency.PLN,
                BigDecimal.valueOf(4324.756752))
        }
    }

    private fun checkCurrencyExchangeIsEqualTo(
        calculatedCurrencyPrice: CalculatedCurrencyPrice?,
        expectedCurrency: Currency,
        expectedPrice: Price
    ) {
        assertThat(calculatedCurrencyPrice?.currency)
            .isEqualTo(expectedCurrency)
        assertThat(calculatedCurrencyPrice?.value)
            .isEqualToIgnoringScale(expectedPrice)
    }
}
