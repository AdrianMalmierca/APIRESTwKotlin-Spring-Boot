package com.amm.qualityproject.domain.model

//for more security
sealed class SubscriptionStatus {

    //singleton object, so there's only one instance of the object
    object Active : SubscriptionStatus()
    object Paused : SubscriptionStatus()
    object Cancelled : SubscriptionStatus()
}