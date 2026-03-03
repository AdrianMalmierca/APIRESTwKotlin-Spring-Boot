package com.amm.test.unit.domain

import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.test.domain.model.Subscription
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class SubscriptionTest {

    @Test
    fun `should create active subscription when price is positive`() {
        val subscription = Subscription.create(
            monthlyPrice = BigDecimal("29.99")
        )

        assertEquals(SubscriptionStatus.Active, subscription.status)
        assertEquals(BigDecimal("29.99"), subscription.monthlyPrice)
        assertNotNull(subscription.id)
    }

    @Test
    fun `should throw exception when price is zero`() {
        assertThrows(IllegalArgumentException::class.java) {
            Subscription.create(BigDecimal.ZERO)
        }
    }

    @Test
    fun `should pause an active subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        subscription.pause()

        assertEquals(SubscriptionStatus.Paused, subscription.status)
    }

    @Test
    fun `should not allow pausing a cancelled subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))
        subscription.cancel()

        assertThrows(IllegalStateException::class.java) {
            subscription.pause()
        }
    }

    @Test
    fun `should set cancelledAt when subscription is cancelled`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        subscription.cancel()

        assertNotNull(subscription.cancelledAt)
        assertEquals(SubscriptionStatus.Cancelled, subscription.status)
    }
}