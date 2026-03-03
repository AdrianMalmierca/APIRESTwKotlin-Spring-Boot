package com.amm.qualityproject.application.subscription

import com.amm.qualityproject.domain.repository.SubscriptionRepository
import com.amm.test.domain.model.Subscription
import java.math.BigDecimal

class CreateSubscriptionUseCase(
    private val repository: SubscriptionRepository
) {

    fun execute(monthlyPrice: BigDecimal): Subscription {
        val subscription = Subscription.create(monthlyPrice)
        repository.save(subscription)
        return subscription
    }
}