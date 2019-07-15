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

// TODO testing of viewmodel
// mock API with Dagger
class CurrencyRateViewModelTest {

//    private var api = mock(RevolutApi::class.java)
//    private var viewModel: CurrencyRateViewModel
//
//    init {
//        viewModel = CurrencyRateViewModel(api)
//    }
//
//    @Before
//    fun init() {
//        `when`(api.get(Currency.EUR.name))
//            .thenReturn(Single.just(currencyRateResponseForEUR))
//        `when`(api.get(Currency.USD.name))
//            .thenReturn(Single.just(currencyRateResponseForEUR))
//    }
//
//    @Test
//    fun switchBaseCurrencyFromDefaultToUSD() {
//        val chosenBaseCurrency = Currency.USD
//        viewModel.setBaseCurrency(chosenBaseCurrency)
//        val estimatedCurrenciesExchange = viewModel.estimatedCurrenciesExchange.value
//        assertEquals(estimatedCurrenciesExchange?.isNotEmpty(), true)
//        assertEquals(chosenBaseCurrency, estimatedCurrenciesExchange?.get(0)?.currency)
//    }
//
//    private val currencyRateResponseForEUR = CurrencyRateResponse()
//        .apply { base = "EUR"; rates = hashMapOf(
//            Pair("USD", 1.1652f),
//            Pair("PLN", 4.3248f)
//        )}
//
//    private val currencyRateResponseForUSD = CurrencyRateResponse()
//        .apply { base = "USD"; rates = hashMapOf(
//            Pair("EUR", 0.85646f),
//            Pair("PLN", 3.6984f)
//        )}
}
