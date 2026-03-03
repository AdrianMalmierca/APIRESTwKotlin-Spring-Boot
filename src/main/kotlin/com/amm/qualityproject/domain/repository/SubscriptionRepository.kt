package com.amm.qualityproject.domain.repository

import com.amm.test.domain.model.Subscription
import java.util.UUID

interface SubscriptionRepository {
    fun save(subscription: Subscription)
    fun findById(id: String): Subscription?
}