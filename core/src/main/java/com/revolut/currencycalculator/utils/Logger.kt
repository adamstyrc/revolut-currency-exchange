package com.revolut.currencycalculator.utils

import android.util.Log
import com.revolut.currencycalculator.BuildConfig
import java.util.*

object Logger {

    fun log(message: String) {
        if (BuildConfig.DEBUG) {
            val second = Calendar.getInstance().get(Calendar.SECOND)
            val millisecond = Calendar.getInstance().get(Calendar.MILLISECOND)
            Log.d("CurrencyCalculator", "[$second:$millisecond] $message")
        }
    }
}