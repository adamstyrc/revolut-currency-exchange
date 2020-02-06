package com.revolut.domain.model

import com.revolut.domain.Price
import java.math.BigDecimal

data class EstimatedCurrencyExchange(
    var currency: Currency,
    var value: Price = BigDecimal.ZERO
)