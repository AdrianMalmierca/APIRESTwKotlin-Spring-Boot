package com.amm.qualityproject.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MonthlyPriceTest {

    @Test
    fun `test getValue and constructor`() {
        val price = MonthlyPrice(BigDecimal.TEN)
        assertEquals(BigDecimal.TEN, price.value)
    }
}