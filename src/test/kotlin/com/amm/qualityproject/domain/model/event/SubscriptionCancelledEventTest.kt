package com.amm.qualityproject.domain.model.event

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.UUID

class SubscriptionCancelledEventTest {

    @Test
    fun `create event and access getters`() {
        val id = UUID.randomUUID()
        val now = Instant.now()
        val event = SubscriptionCancelledEvent(id, now)

        assertEquals(id, event.subscriptionId)
        assertEquals(now, event.cancelledAt)
    }
}