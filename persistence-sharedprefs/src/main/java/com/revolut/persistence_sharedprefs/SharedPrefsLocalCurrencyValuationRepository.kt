package com.revolut.persistence_sharedprefs

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.revolut.domain.model.CurrencyValuation
import com.revolut.ports.LocalCurrencyValuationRepository

class SharedPrefsLocalCurrencyValuationRepository(
    context: Context
) : LocalCurrencyValuationRepository {

    companion object {
        const val SHARED_PREFERENCES_NAME = "revolut.sharedpref"
        const val LATEST_CURRENCY_RATES = "LATEST_CURRENCY_RATES"
    }

    private val sharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE)
    private val gson = Gson()

    private val currencyValuationLiveData =
        MutableLiveData<CurrencyValuation>()
            .apply {
                val latestCurrencyValuationJson =
                    sharedPreferences.getString(LATEST_CURRENCY_RATES, null)
                if (latestCurrencyValuationJson != null) {
                    try {
                        value = gson.fromJson(
                            latestCurrencyValuationJson,
                            CurrencyValuation::class.java
                        )
                    } catch (e: Exception) {
                    }
                }
            }

    override fun getCurrencyValuation(): LiveData<CurrencyValuation> {
        return currencyValuationLiveData
    }

    override fun saveCurrencyValuation(currencyValuation: CurrencyValuation) {
        currencyValuationLiveData.postValue(currencyValuation)

        val json = gson.toJson(currencyValuation)
        sharedPreferences.edit().putString(LATEST_CURRENCY_RATES, json).apply()
    }
}