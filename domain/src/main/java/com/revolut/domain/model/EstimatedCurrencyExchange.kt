package com.revolut.domain.model

import com.revolut.domain.Money
import java.math.BigDecimal

class EstimatedCurrencyExchange(
    var currency: Currency,
    var value: Money = BigDecimal.ZERO
)