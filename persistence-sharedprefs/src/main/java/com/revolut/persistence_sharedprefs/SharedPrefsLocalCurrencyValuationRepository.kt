package com.revolut.persistence_sharedprefs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.revolut.domain.model.CurrencyValuation
import com.revolut.ports.LocalCurrencyValuationRepository

class SharedPrefsLocalCurrencyValuationRepository : LocalCurrencyValuationRepository {

    private val currencyValuationLiveData = MutableLiveData<CurrencyValuation>()

    override fun getCurrencyValuation(): LiveData<CurrencyValuation> {
        return currencyValuationLiveData
    }

    override fun saveCurrencyValuation(currencyValuation: CurrencyValuation) {
        currencyValuationLiveData.postValue(currencyValuation)
    }
}