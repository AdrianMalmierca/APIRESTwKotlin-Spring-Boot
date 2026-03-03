package com.amm.qualityproject.domain.model.implementation

import com.amm.qualityproject.domain.model.SubscriptionStatus
import com.amm.qualityproject.domain.model.interfaces.CancellationPolicy
import com.amm.test.domain.model.Subscription

class DefaultCancellationPolicy : CancellationPolicy {
    override fun canCancel(subscription: Subscription): Boolean {
        return subscription.status !is SubscriptionStatus.Cancelled //true if is not cancelled
    }
}