package com.amm.qualityproject.integration

import com.amm.qualityproject.application.subscription.CancelSubscriptionUseCase
import com.amm.qualityproject.application.subscription.CreateSubscriptionUseCase
import com.amm.qualityproject.application.subscription.PauseSubscriptionUseCase
import com.amm.qualityproject.application.subscription.ResumeSubscriptionUseCase
import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.infraestructure.persistence.adapter.JpaSubscriptionRepository
import com.amm.qualityproject.infraestructure.persistence.repository.SpringDataSubscriptionRepository
import com.amm.test.application.subscription.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal

@SpringBootTest
@Testcontainers
class SubscriptionIntegrationTest(
    private val springDataRepository: SpringDataSubscriptionRepository
) {

    companion object {

        @Container
        val postgres = PostgreSQLContainer("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")

        @JvmStatic
        @DynamicPropertySource
        fun configure(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Test
    fun `full lifecycle integration test`() {

        val repository = JpaSubscriptionRepository(springDataRepository)

        val create = CreateSubscriptionUseCase(repository)
        val pause = PauseSubscriptionUseCase(repository)
        val resume = ResumeSubscriptionUseCase(repository)
        val cancel = CancelSubscriptionUseCase(repository)

        // CREATE
        val subscription = create.execute(BigDecimal("29.99"))
        assertEquals(SubscriptionStatus.Active, subscription.status)

        // PAUSE
        pause.execute(subscription.id.toString())
        val paused = repository.findById(subscription.id.toString())!!
        assertEquals(SubscriptionStatus.Paused, paused.status)

        // RESUME
        resume.execute(subscription.id.toString())
        val resumed = repository.findById(subscription.id.toString())!!
        assertEquals(SubscriptionStatus.Active, resumed.status)

        // CANCEL
        cancel.execute(subscription.id.toString())
        val cancelled = repository.findById(subscription.id.toString())!!
        assertEquals(SubscriptionStatus.Cancelled, cancelled.status)
    }
}