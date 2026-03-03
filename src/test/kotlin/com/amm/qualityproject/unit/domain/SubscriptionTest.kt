package com.amm.test.unit.domain

import com.amm.qualityproject.domain.model.MonthlyPrice
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.domain.model.event.SubscriptionCancelledEvent
import com.amm.test.domain.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class SubscriptionTest {

    @Test
    fun `should create active subscription when price is positive`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        assertEquals(SubscriptionStatus.Active, subscription.status)
        assertEquals(MonthlyPrice(BigDecimal("29.99")), subscription.monthlyPrice)
        assertNotNull(subscription.id)
        assertNull(subscription.cancelledAt)
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
    fun `should resume a paused subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        subscription.pause()
        subscription.resume()

        assertEquals(SubscriptionStatus.Active, subscription.status)
    }

    @Test
    fun `should not allow resuming an active subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        assertThrows(IllegalStateException::class.java) {
            subscription.resume()
        }
    }

    @Test
    fun `should not allow resuming a cancelled subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))
        subscription.cancel()

        assertThrows(IllegalStateException::class.java) {
            subscription.resume()
        }
    }

    @Test
    fun `should cancel an active subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        subscription.cancel()

        assertEquals(SubscriptionStatus.Cancelled, subscription.status)
        assertNotNull(subscription.cancelledAt)

        //Placeholder for Domain Event
        val event = SubscriptionCancelledEvent(subscription.id, subscription.cancelledAt!!)
        assertEquals(subscription.id, event.subscriptionId)
    }

    @Test
    fun `should cancel a paused subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))
        subscription.pause()

        subscription.cancel()

        assertEquals(SubscriptionStatus.Cancelled, subscription.status)
        assertNotNull(subscription.cancelledAt)
    }

    @Test
    fun `should not allow cancelling a cancelled subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))
        subscription.cancel()

        assertThrows(IllegalStateException::class.java) {
            subscription.cancel()
        }
    }
}