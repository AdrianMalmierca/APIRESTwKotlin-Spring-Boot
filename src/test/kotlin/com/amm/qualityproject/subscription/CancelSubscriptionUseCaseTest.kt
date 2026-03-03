package com.amm.test.application.subscription

import com.amm.qualityproject.application.subscription.CancelSubscriptionUseCase
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.domain.repository.SubscriptionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import com.amm.test.domain.model.Subscription
import org.mockito.Mockito.*

class CancelSubscriptionUseCaseTest {

    private val repository: SubscriptionRepository = mock()
    private val useCase = CancelSubscriptionUseCase(repository)

    @Test
    fun `should cancel a subscription and save in repository`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        `when`(repository.findById(subscription.id.toString())).thenReturn(subscription)

        useCase.execute(subscription.id.toString())

        assertEquals(SubscriptionStatus.Cancelled, subscription.status)
        verify(repository).save(subscription)
    }
}