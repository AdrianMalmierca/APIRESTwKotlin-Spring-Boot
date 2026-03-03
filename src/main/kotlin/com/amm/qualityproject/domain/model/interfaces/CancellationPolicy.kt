package com.amm.qualityproject.domain.model.interfaces

import com.amm.test.domain.model.Subscription

interface CancellationPolicy {
    fun canCancel(subscription: Subscription): Boolean
}