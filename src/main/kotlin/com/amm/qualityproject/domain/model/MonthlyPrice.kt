package com.amm.qualityproject.domain.model

import java.math.BigDecimal

@JvmInline
value class MonthlyPrice(val value: BigDecimal) {
    init {
        require(value > BigDecimal.ZERO) { "Monthly price must be positive" }
    }
}