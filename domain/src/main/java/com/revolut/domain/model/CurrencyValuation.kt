package com.revolut.domain.model

import com.revolut.domain.Money

data class CurrencyValuation(
    var base: Currency,
    var date: String? = null,
    var rates: Map<Currency, Money>
)