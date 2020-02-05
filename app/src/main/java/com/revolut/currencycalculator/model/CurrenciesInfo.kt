package com.revolut.currencycalculator.model

import com.revolut.currencycalculator.R
import com.revolut.domain.model.Currency

object CurrenciesInfo {

    private val currencyToIcon = hashMapOf(
        Currency.USD to CurrencyInfo(R.drawable.ic_usd, R.string.currency_usd),
        Currency.EUR to CurrencyInfo(R.drawable.ic_eur, R.string.currency_eur),
        Currency.RUB to CurrencyInfo(R.drawable.ic_rub, R.string.currency_rub),
        Currency.GBP to CurrencyInfo(R.drawable.ic_gbp, R.string.currency_gbp),
        Currency.PLN to CurrencyInfo(R.drawable.ic_pln, R.string.currency_pln),
        Currency.AUD to CurrencyInfo(R.drawable.ic_aud, R.string.currency_aud),
        Currency.BGN to CurrencyInfo(R.drawable.ic_bgn, R.string.currency_bgn),
        Currency.BRL to CurrencyInfo(R.drawable.ic_brl, R.string.currency_brl),
        Currency.CAD to CurrencyInfo(R.drawable.ic_cad, R.string.currency_cad),
        Currency.CHF to CurrencyInfo(R.drawable.ic_chf, R.string.currency_chf),
        Currency.CZK to CurrencyInfo(R.drawable.ic_czk, R.string.currency_czk),
        Currency.CNY to CurrencyInfo(R.drawable.ic_cny, R.string.currency_cny),
        Currency.DKK to CurrencyInfo(R.drawable.ic_dkk, R.string.currency_dkk),
        Currency.HKD to CurrencyInfo(R.drawable.ic_hkg, R.string.currency_hkg),
        Currency.HRK to CurrencyInfo(R.drawable.ic_hrk, R.string.currency_hrk),
        Currency.HUF to CurrencyInfo(R.drawable.ic_huf, R.string.currency_huf),
        Currency.IDR to CurrencyInfo(R.drawable.ic_idr, R.string.currency_idr),
        Currency.ILS to CurrencyInfo(R.drawable.ic_ils, R.string.currency_ils),
        Currency.INR to CurrencyInfo(R.drawable.ic_inr, R.string.currency_inr),
        Currency.ISK to CurrencyInfo(R.drawable.ic_isk, R.string.currency_isk),
        Currency.JPY to CurrencyInfo(R.drawable.ic_jpy, R.string.currency_jpy),
        Currency.KRW to CurrencyInfo(R.drawable.ic_krw, R.string.currency_krw),
        Currency.MXN to CurrencyInfo(R.drawable.ic_mxn, R.string.currency_mxn),
        Currency.MYR to CurrencyInfo(R.drawable.ic_myr, R.string.currency_myr),
        Currency.NOK to CurrencyInfo(R.drawable.ic_nok, R.string.currency_nok),
        Currency.NZD to CurrencyInfo(R.drawable.ic_nzd, R.string.currency_nzd),
        Currency.PHP to CurrencyInfo(R.drawable.ic_php, R.string.currency_php),
        Currency.RON to CurrencyInfo(R.drawable.ic_ron, R.string.currency_ron),
        Currency.SEK to CurrencyInfo(R.drawable.ic_sek, R.string.currency_sek),
        Currency.SGD to CurrencyInfo(R.drawable.ic_currency_unknown, R.string.currency_sgd),
        Currency.THB to CurrencyInfo(R.drawable.ic_currency_unknown, R.string.currency_thb),
        Currency.TRY to CurrencyInfo(R.drawable.ic_currency_unknown, R.string.currency_try),
        Currency.ZAR to CurrencyInfo(R.drawable.ic_currency_unknown, R.string.currency_zar)
    )

    fun getIcon(currency: Currency): Int?
            = currencyToIcon[currency]?.iconResource

    fun getNameRes(currency: Currency): Int?
            = currencyToIcon[currency]?.nameResource

}