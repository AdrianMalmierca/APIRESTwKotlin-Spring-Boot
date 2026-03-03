package com.amm.qualityproject.domain.model

sealed class SubscriptionStatus {

    object Active : SubscriptionStatus()
    object Paused : SubscriptionStatus()
    object Cancelled : SubscriptionStatus()
}