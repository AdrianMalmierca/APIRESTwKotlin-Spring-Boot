package com.amm.qualityproject.integration

import com.amm.qualityproject.domain.model.implementation.DefaultCancellationPolicy
import com.amm.test.domain.model.Subscription
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DefaultCancellationPolicyTest {

    private val policy = DefaultCancellationPolicy()

    @Test
    fun `should allow cancelling active subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))
        assertTrue(policy.canCancel(subscription))
    }

    @Test
    fun `should allow cancelling paused subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))
        subscription.pause()
        assertTrue(policy.canCancel(subscription))
    }

    @Test
    fun `should not allow cancelling cancelled subscription`() {
        val subscription = Subscription.create(BigDecimal("29.99"))
        subscription.cancel()
        assertFalse(policy.canCancel(subscription))
    }
}