package com.amm.test.application.subscription

import com.amm.qualityproject.application.subscription.CreateSubscriptionUseCase
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.domain.repository.SubscriptionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.math.BigDecimal

class CreateSubscriptionUseCaseTest {

    private val repository: SubscriptionRepository = mock()
    private val useCase = CreateSubscriptionUseCase(repository)

    @Test
    fun `should create subscription and save it in repository`() {
        val monthlyPrice = BigDecimal("29.99")

        val subscription = useCase.execute(monthlyPrice)

        //Check  that the subscription is created correctly
        assertEquals(monthlyPrice, subscription.monthlyPrice.value)
        assertEquals(SubscriptionStatus.Active, subscription.status)

        //Check that we saved the subscription in the repository
        verify(repository).save(subscription)
    }
}