package com.example.expenzify.data.model

data class ExpenseSummary(
    val type: String,
    val date: Long,
    val total_amount: Double
)