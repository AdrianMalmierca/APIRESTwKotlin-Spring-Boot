package com.amm.test.unit.domain

import com.amm.qualityproject.domain.model.MonthlyPrice
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.domain.model.event.SubscriptionCancelledEvent
import com.amm.qualityproject.domain.model.interfaces.CancellationPolicy
import com.amm.test.domain.model.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.time.Instant

class SubscriptionTest {

    private val dummyPolicy = mock<CancellationPolicy>()

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

    @Test
    fun `cancelledAt should be set to current time`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        val before = Instant.now()

        subscription.cancel()

        val after = Instant.now()

        assertNotNull(subscription.cancelledAt)
        assertTrue(subscription.cancelledAt!!.isAfter(before.minusSeconds(1)))
        assertTrue(subscription.cancelledAt!!.isBefore(after.plusSeconds(1)))
    }


    @Test
    fun `test pause and resume subscription`() {
        val subscription = Subscription.create(BigDecimal.TEN)

        // Pausa
        subscription.pause()
        assertEquals(SubscriptionStatus.Paused::class, subscription.status::class)

        // Resume
        subscription.resume()
        assertEquals(SubscriptionStatus.Active::class, subscription.status::class)
    }

    @Test
    fun `test cancel subscription using default`() {
        val subscription = Subscription.create(BigDecimal.TEN)

        // Mockito way
        `when`(dummyPolicy.canCancel(subscription)).thenReturn(true)

        subscription.cancel(dummyPolicy)

        assertEquals(SubscriptionStatus.Cancelled::class, subscription.status::class)
        assertNotNull(subscription.cancelledAt)
    }

    @Test
    fun `test setCancelledAt explicitly`() {
        val subscription = Subscription.create(BigDecimal.TEN)
        val now = Instant.now()
        subscription.cancelledAt = now  // usa el setter de Kotlin
        assertEquals(now, subscription.cancelledAt)
    }
}