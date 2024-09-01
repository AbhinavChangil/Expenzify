package com.example.expenzify.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//ye system me necessary classes ko generate krta hai
@Entity(tableName = "expense_table")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val type: String,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String,
    val description: String

)
