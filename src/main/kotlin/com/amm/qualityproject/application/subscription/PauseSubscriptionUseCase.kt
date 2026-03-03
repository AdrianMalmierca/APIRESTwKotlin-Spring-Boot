package com.amm.qualityproject.application.subscription

import com.amm.qualityproject.domain.repository.SubscriptionRepository
import java.util.UUID

class PauseSubscriptionUseCase(private val repository: SubscriptionRepository) {
    fun execute(subscriptionId: String) {
        val subscription = repository.findById(subscriptionId)
            ?: throw IllegalArgumentException("Subscription not found")
        subscription.pause()
        repository.save(subscription)
    }
}