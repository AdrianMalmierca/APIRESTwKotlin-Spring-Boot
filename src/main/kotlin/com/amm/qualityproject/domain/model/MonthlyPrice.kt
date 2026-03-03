package com.amm.qualityproject.domain.model

import java.math.BigDecimal

@JvmInline //when is executed there's not MonthlyPrice separated, just the BigDecimal
value class MonthlyPrice(val value: BigDecimal) {
    init {
        //when is executed we checked if the value is more than 0
        require(value > BigDecimal.ZERO) { "Monthly price must be positive" }
    }
}