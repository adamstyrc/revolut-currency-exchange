package com.revolut.domain.formatter

import com.revolut.domain.Price
import java.text.DecimalFormat

object PriceFormatter {

    private val decimalFormat = DecimalFormat("0.##")

    fun displayPriceWith2Decimals(price: Price): String {
        return "%.2f".format(price)
            .replace(",", ".")
    }

    fun displayPriceUpTo2Decimals(price: Price): String {
        return decimalFormat.format(price)
    }
}