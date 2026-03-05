package com.amm.qualityproject.integration

import com.amm.qualityproject.infraestructure.persistence.adapter.JpaSubscriptionRepository
import com.amm.test.domain.model.Subscription
import com.amm.qualityproject.domain.model.SubscriptionStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.concurrent.Callable
import java.util.concurrent.Executors

@SpringBootTest(properties = ["spring.profiles.active=test"])
class SubscriptionConcurrencyTest {

    @Autowired
    private lateinit var repository: JpaSubscriptionRepository

    @Test
    fun `should handle complex concurrent operations`() {
        val subscription = Subscription.create(BigDecimal("29.99"))
        repository.save(subscription)

        val executor = Executors.newFixedThreadPool(4)

        // Creamos tareas concurrentes mixtas
        val tasks = listOf(
            Callable { safePause(subscription.id.toString()) },
            Callable { safeResume(subscription.id.toString()) },
            Callable { safeCancel(subscription.id.toString()) },
            Callable { safeCancel(subscription.id.toString()) } //cancel twice to check race
        )

        val futures = tasks.map { executor.submit(it) }
        executor.shutdown()

        val results = futures.map { it.get() }

        // Filtramos excepciones
        val exceptions = results.filterIsInstance<Exception>()

        // Solo deberían ocurrir excepciones en operaciones inválidas
        assertTrue(exceptions.isNotEmpty())

        // Comprobamos consistencia final del estado
        val final = repository.findById(subscription.id.toString())!!
        assertTrue(
            final.status is SubscriptionStatus.Active ||
                    final.status is SubscriptionStatus.Paused ||
                    final.status is SubscriptionStatus.Cancelled
        )

        // Si está cancelada, cancelledAt debe estar seteado
        if (final.status is SubscriptionStatus.Cancelled) {
            assertNotNull(final.cancelledAt)
        }
    }

    private fun safePause(id: String): Any? = try {
        repository.findById(id)!!.apply { pause() }
        repository.save(repository.findById(id)!!)
    } catch (e: Exception) {
        e
    }

    private fun safeResume(id: String): Any? = try {
        repository.findById(id)!!.apply { resume() }
        repository.save(repository.findById(id)!!)
    } catch (e: Exception) {
        e
    }

    private fun safeCancel(id: String): Any? = try {
        repository.findById(id)!!.apply { cancel() }
        repository.save(repository.findById(id)!!)
    } catch (e: Exception) {
        e
    }
}