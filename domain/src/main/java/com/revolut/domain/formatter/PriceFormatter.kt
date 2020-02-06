package com.revolut.domain.formatter

import com.revolut.domain.Price
import java.math.BigDecimal

object PriceFormatter {

    fun displayPriceWith2Decimals(price: Price): String {
        return "%.2f".format(price)
            .replace(",", ".")
    }

    fun displayPriceUpTo2Decimals(price: Price): String {
        val priceInteger = price.toLong()
        val fraction = price.minus(Price.valueOf(priceInteger))

        if (fraction.compareTo(Price.ZERO) == 0) {
            return priceInteger.toString()
        } else if (fraction.precision() == 1) {
            return "%.1f".format(price)
                .replace(",", ".")
        } else {
            return displayPriceWith2Decimals(price)
        }

    }
}