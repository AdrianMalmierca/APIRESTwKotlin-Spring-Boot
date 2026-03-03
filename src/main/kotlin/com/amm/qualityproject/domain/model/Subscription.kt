package com.amm.test.domain.model

import com.amm.qualityproject.domain.model.MonthlyPrice
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.domain.model.implementation.DefaultCancellationPolicy
import com.amm.qualityproject.domain.model.interfaces.CancellationPolicy
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class Subscription private constructor(
    val id: UUID,
    val monthlyPrice: MonthlyPrice,
    private var _status: SubscriptionStatus,
    var cancelledAt: Instant?
) {

    val status: SubscriptionStatus get() = _status

    companion object {
        fun create(monthlyPrice: BigDecimal): Subscription {
            require(monthlyPrice > BigDecimal.ZERO) {
                "Monthly price must be positive"
            }

            return Subscription(
                id = UUID.randomUUID(),
                monthlyPrice = MonthlyPrice(monthlyPrice),
                _status = SubscriptionStatus.Active,
                cancelledAt = null
            )
        }
    }

    fun pause() {
        _status = when (_status) {
            is SubscriptionStatus.Active -> SubscriptionStatus.Paused
            is SubscriptionStatus.Paused -> throw IllegalStateException("Already paused")
            is SubscriptionStatus.Cancelled -> throw IllegalStateException("Cannot pause cancelled subscription")
        }
    }

    fun resume() {
        _status = when (_status) {
            is SubscriptionStatus.Paused -> SubscriptionStatus.Active
            is SubscriptionStatus.Active -> throw IllegalStateException("Already active")
            is SubscriptionStatus.Cancelled -> throw IllegalStateException("Cannot resume cancelled subscription")
        }
    }

    fun cancel(policy: CancellationPolicy = DefaultCancellationPolicy()) {
        if (!policy.canCancel(this)) throw IllegalStateException("Cannot cancel")
        cancelledAt = Instant.now()
        _status = SubscriptionStatus.Cancelled
    }
}