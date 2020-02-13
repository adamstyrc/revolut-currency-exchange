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

        val twoDecimalsAsInt = fraction.multiply(BigDecimal.valueOf(100)).toInt()

        if (twoDecimalsAsInt % 100 == 0) {
            return priceInteger.toString()
        } else if (twoDecimalsAsInt % 10 == 0) {
            return "%.1f".format(price)
                .replace(",", ".")
        } else {
            return displayPriceWith2Decimals(price)
        }
    }
}