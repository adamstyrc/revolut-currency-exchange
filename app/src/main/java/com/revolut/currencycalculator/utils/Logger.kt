package com.revolut.currencycalculator.utils

import android.util.Log
import com.revolut.currencycalculator.BuildConfig

object Logger {

    fun log(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d("CurrencyCalculator", message)
        }
    }
}