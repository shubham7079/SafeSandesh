package com.example.data.repository

object BillingRepository {

    const val PRICE_MONTHLY = "Rs. 99 / month"
    const val PRICE_YEARLY = "Rs. 799 / year"
    const val YEARLY_SUBLINE = "Less than Rs. 3 a day"

    fun getPrices(): Map<String, String> {
        return mapOf(
            "monthly" to PRICE_MONTHLY,
            "yearly" to PRICE_YEARLY
        )
    }

    suspend fun purchase(@Suppress("UNUSED_PARAMETER") planId: String): Boolean {
        // Subscriptions placeholder until store billing integration is connected
        return false
    }

    suspend fun restore(): Boolean {
        return false
    }
}
