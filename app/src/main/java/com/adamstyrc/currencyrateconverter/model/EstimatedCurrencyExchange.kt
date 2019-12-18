package com.adamstyrc.currencyrateconverter.model

import java.math.BigDecimal

class EstimatedCurrencyExchange(
    var currency: Currency,
    var value: Money = BigDecimal.ZERO
)
