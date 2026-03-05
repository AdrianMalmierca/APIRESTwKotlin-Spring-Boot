package com.amm.qualityproject.integration

import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.infraestructure.persistence.adapter.JpaSubscriptionRepository
import com.amm.qualityproject.infraestructure.persistence.entity.JpaSubscriptionEntity
import com.amm.qualityproject.infraestructure.persistence.repository.SpringDataSubscriptionRepository
import com.amm.test.domain.model.Subscription
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.Instant

@SpringBootTest(properties = ["spring.profiles.active=test"])
class JpaSubscriptionRepositoryIntegrationTest() {

    @Autowired
    private lateinit var springDataRepository: SpringDataSubscriptionRepository

    private lateinit var repository: JpaSubscriptionRepository

    @BeforeEach
    fun setup() {
        repository = JpaSubscriptionRepository(springDataRepository)
    }

    @Test
    fun `should persist and retrieve subscription correctly`() {
        val subscription = Subscription.create(BigDecimal("29.99"))

        repository.save(subscription)

        val fromDb = repository.findById(subscription.id.toString())

        assertNotNull(fromDb)
        assertEquals(subscription.id, fromDb!!.id)
        assertEquals(subscription.monthlyPrice.value, fromDb.monthlyPrice.value)
        assertEquals(subscription.status, fromDb.status)
        assertNull(fromDb.cancelledAt)
    }


    @Test
    fun `should map status correctly between domain and database`() {
        val subscription = Subscription.create(BigDecimal("29.99"))
        subscription.pause()

        repository.save(subscription)

        val entity = springDataRepository.findById(subscription.id.toString()).get()

        assertEquals("Paused", entity.status)

        val restored = repository.findById(subscription.id.toString())

        assertEquals(SubscriptionStatus.Paused, restored!!.status)
    }


    @Test
    fun `test setters and getters`() {
        val entity = JpaSubscriptionEntity(
            id = "123",
            monthlyPrice = BigDecimal.TEN,
            status = "Active",
            cancelledAt = null
        )

        entity.id = "456"
        entity.monthlyPrice = BigDecimal.valueOf(20)
        entity.status = "Paused"
        val now = Instant.now()
        entity.cancelledAt = now

        assertEquals("456", entity.id)
        assertEquals(BigDecimal.valueOf(20), entity.monthlyPrice)
        assertEquals("Paused", entity.status)
        assertEquals(now, entity.cancelledAt)
    }
}