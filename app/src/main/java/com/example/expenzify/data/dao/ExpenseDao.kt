package com.example.expenzify.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expenzify.Utils
import com.example.expenzify.data.model.ExpenseEntity
import com.example.expenzify.data.model.ExpenseSummary
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT type, date, SUM(amount) AS total_amount FROM expense_table where type = :type GROUP BY type, date ORDER BY date ASC")
    fun getAllExpensesByDate(type: String): Flow<List<ExpenseSummary>>

    @Query("SELECT * FROM expense_table WHERE type = :type ORDER BY amount DESC LIMIT 5")
    fun getTopExpensesByType(type: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense_table ORDER BY amount DESC LIMIT 5")
    fun getTopTransactions(): Flow<List<ExpenseEntity>>

    @Insert
    suspend fun insertExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

}