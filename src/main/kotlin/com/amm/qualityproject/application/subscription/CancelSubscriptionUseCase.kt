package com.amm.qualityproject.application.subscription

import com.amm.qualityproject.domain.model.implementation.DefaultCancellationPolicy
import com.amm.qualityproject.domain.repository.SubscriptionRepository

class CancelSubscriptionUseCase(private val repository: SubscriptionRepository) {
    fun execute(subscriptionId: String) {
        val subscription = repository.findById(subscriptionId)
            ?: throw IllegalArgumentException("Subscription not found")
        subscription.cancel(DefaultCancellationPolicy())
        repository.save(subscription)
    }
}