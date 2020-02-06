package com.revolut.domain.formatter

import com.google.common.truth.Truth.assertThat
import com.revolut.domain.Price
import org.junit.Test


class PriceFormatterTest {

    @Test
    fun displayPriceUpTo2Decimals_integers() {
        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(12)))
            .isEqualTo("12")

        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(0)))
            .isEqualTo("0")
        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(0.00)))
            .isEqualTo("0")

        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(101)))
            .isEqualTo("101")

        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(9909.0)))
            .isEqualTo("9909")


    }

    @Test
    fun displayPriceUpTo2Decimals_1decimal() {
        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(7.10)))
            .isEqualTo("7.1")

        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(0.9)))
            .isEqualTo("0.9")

        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(150.5)))
            .isEqualTo("150.5")
    }

    @Test
    fun displayPriceUpTo2Decimals_2decimals() {
        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(7.12)))
            .isEqualTo("7.12")

        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(0.99)))
            .isEqualTo("0.99")

        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(1010.55)))
            .isEqualTo("1010.55")
    }

    @Test
    fun displayPriceUpTo2Decimals_many_decimals() {
        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(7.1212)))
            .isEqualTo("7.12")

        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(0.9904)))
            .isEqualTo("0.99")

        assertThat(PriceFormatter.displayPriceUpTo2Decimals(Price.valueOf(1010.5533)))
            .isEqualTo("1010.55")
    }
}