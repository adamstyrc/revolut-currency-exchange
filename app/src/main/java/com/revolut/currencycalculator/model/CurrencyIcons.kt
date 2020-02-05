package com.revolut.currencycalculator.model

import com.revolut.currencycalculator.R
import com.revolut.domain.model.Currency

object CurrencyIcons {

    private val currencyToIcon = hashMapOf(
        Currency.USD to R.drawable.ic_usd,
        Currency.EUR to R.drawable.ic_eur,
        Currency.CHF to R.drawable.ic_chf,
        Currency.AUD to R.drawable.ic_aud,
        Currency.CAD to R.drawable.ic_cad,
        Currency.RUB to R.drawable.ic_rub,
        Currency.JPY to R.drawable.ic_jpy,
        Currency.SEK to R.drawable.ic_sek
    )

    fun getIcon(currency: Currency)
            = currencyToIcon[currency] ?: R.drawable.ic_currency_unknown
}