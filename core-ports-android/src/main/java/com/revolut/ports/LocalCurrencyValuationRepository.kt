package com.revolut.ports

import androidx.lifecycle.LiveData
import com.revolut.domain.model.CurrencyValuation

interface LocalCurrencyValuationRepository {

    fun getCurrencyValuation(): LiveData<CurrencyValuation>

    fun saveCurrencyValuation(currencyValuation: CurrencyValuation)
}