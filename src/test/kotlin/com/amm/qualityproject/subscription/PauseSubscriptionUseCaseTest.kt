package com.amm.test.application.subscription

import com.amm.qualityproject.application.subscription.PauseSubscriptionUseCase
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.domain.repository.SubscriptionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.math.BigDecimal
import com.amm.test.domain.model.Subscription

class PauseSubscriptionUseCaseTest {

    private val repository: SubscriptionRepository = mock()
    private val useCase = PauseSubscriptionUseCase(repository)

    @Test
    fun `should pause an active subscription and save it in repository`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        `when`(repository.findById(subscription.id.toString())).thenReturn(subscription)

        useCase.execute(subscription.id.toString())

        assertEquals(SubscriptionStatus.Paused, subscription.status)
        verify(repository).save(subscription)
    }
}