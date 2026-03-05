package com.amm.qualityproject.subscription

import com.amm.qualityproject.application.subscription.CreateSubscriptionUseCase
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.domain.repository.SubscriptionRepository
import com.amm.test.domain.model.Subscription
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
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

    @Test
    fun `should throw when price is negative`() {
        val negativePrice = BigDecimal("-10")

        // Verifica que lanza IllegalArgumentException
        assertThrows(IllegalArgumentException::class.java) {
            useCase.execute(negativePrice)
        }

        // Verifica que el repositorio **no fue llamado en absoluto**
        verifyNoInteractions(repository)
    }
}