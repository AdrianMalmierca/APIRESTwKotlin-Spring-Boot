package com.amm.test.domain.model

import com.amm.qualityproject.domain.model.MonthlyPrice
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.domain.model.implementation.DefaultCancellationPolicy
import com.amm.qualityproject.domain.model.interfaces.CancellationPolicy
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

class Subscription private constructor(
    val id: UUID,
    val monthlyPrice: MonthlyPrice,
    private var _status: SubscriptionStatus,
    var cancelledAt: Instant?
) {

    val status: SubscriptionStatus get() = _status

    companion object { //because we need to access to the Subscription attributes

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

        fun restore(
            id: String,
            monthlyPrice: BigDecimal,
            status: String,
            cancelledAt: Instant?
        ): Subscription {

            val subscription = Subscription(
                id = UUID.fromString(id),
                monthlyPrice = MonthlyPrice(monthlyPrice),
                _status = when (status) {
                    "Active" -> SubscriptionStatus.Active
                    "Paused" -> SubscriptionStatus.Paused
                    "Cancelled" -> SubscriptionStatus.Cancelled
                    else -> throw IllegalArgumentException("Unknown status")
                },
                cancelledAt = cancelledAt
            )

            return subscription
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