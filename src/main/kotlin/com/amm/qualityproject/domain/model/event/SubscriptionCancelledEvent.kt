package com.amm.qualityproject.domain.model.event

import java.time.Instant
import java.util.UUID

data class SubscriptionCancelledEvent(
    val subscriptionId: UUID,
    val cancelledAt: Instant
)