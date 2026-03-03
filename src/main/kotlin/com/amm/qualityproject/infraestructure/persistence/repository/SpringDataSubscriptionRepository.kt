package com.amm.qualityproject.infraestructure.persistence.repository


import com.amm.qualityproject.infraestructure.persistence.entity.JpaSubscriptionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SpringDataSubscriptionRepository :
    JpaRepository<JpaSubscriptionEntity, String>