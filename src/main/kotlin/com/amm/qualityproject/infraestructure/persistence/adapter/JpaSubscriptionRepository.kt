package com.amm.qualityproject.infraestructure.persistence.adapter

import com.amm.qualityproject.domain.repository.SubscriptionRepository
import com.amm.qualityproject.infraestructure.persistence.entity.JpaSubscriptionEntity
import com.amm.qualityproject.infraestructure.persistence.repository.SpringDataSubscriptionRepository
import com.amm.test.domain.model.*
import java.util.UUID

class JpaSubscriptionRepository(private val repository: SpringDataSubscriptionRepository)
    : SubscriptionRepository {

    override fun save(subscription: Subscription) {
        val entity = JpaSubscriptionEntity(
            id = subscription.id.toString(),
            monthlyPrice = subscription.monthlyPrice.value,
            status = subscription.status::class.simpleName!!,
            cancelledAt = subscription.cancelledAt
        )

        repository.save(entity)
    }

    override fun findById(id: String): Subscription? {
        val entity = repository.findById(id).orElse(null) ?: return null

        val subscription = Subscription.restore(
            id = entity.id,
            monthlyPrice = entity.monthlyPrice,
            status = entity.status,
            cancelledAt = entity.cancelledAt
        )

        return subscription
    }
}