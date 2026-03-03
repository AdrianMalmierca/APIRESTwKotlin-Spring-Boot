package com.amm.qualityproject.infraestructure.persistence.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime

@Entity
@Table(name = "subscriptions")
class JpaSubscriptionEntity(

    @Id
    var id: String,

    var monthlyPrice: BigDecimal,

    var status: String,

    var cancelledAt: Instant?
)