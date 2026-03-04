package com.amm.qualityproject.integration

import com.amm.qualityproject.application.subscription.*
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.infraestructure.persistence.adapter.JpaSubscriptionRepository
import com.amm.qualityproject.infraestructure.persistence.repository.SpringDataSubscriptionRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest(properties = ["spring.profiles.active=test"])
class SubscriptionIntegrationTest{

    @Autowired
    private lateinit var springDataRepository: SpringDataSubscriptionRepository

    private fun createUseCases(): SubscriptionUseCases {
        val repository = JpaSubscriptionRepository(springDataRepository)
        return SubscriptionUseCases(
            create = CreateSubscriptionUseCase(repository),
            pause = PauseSubscriptionUseCase(repository),
            resume = ResumeSubscriptionUseCase(repository),
            cancel = CancelSubscriptionUseCase(repository),
            repository = repository
        )
    }

    @Test
    fun `full subscription lifecycle`() {
        val price = BigDecimal("29.99")
        val cases = createUseCases()

        // CREATE
        val subscription = cases.create.execute(price)
        assertEquals(SubscriptionStatus.Active, subscription.status)

        // PAUSE
        cases.pause.execute(subscription.id.toString())
        val paused = cases.repository.findById(subscription.id.toString())!!
        assertEquals(SubscriptionStatus.Paused, paused.status)

        // RESUME
        cases.resume.execute(subscription.id.toString())
        val resumed = cases.repository.findById(subscription.id.toString())!!
        assertEquals(SubscriptionStatus.Active, resumed.status)

        // CANCEL
        cases.cancel.execute(subscription.id.toString())
        val cancelled = cases.repository.findById(subscription.id.toString())!!
        assertEquals(SubscriptionStatus.Cancelled, cancelled.status)
    }

    private data class SubscriptionUseCases(
        val create: CreateSubscriptionUseCase,
        val pause: PauseSubscriptionUseCase,
        val resume: ResumeSubscriptionUseCase,
        val cancel: CancelSubscriptionUseCase,
        val repository: JpaSubscriptionRepository
    )
}