package com.example.expenzify.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.expenzify.Utils
import com.example.expenzify.data.ExpenseDatabase
import com.example.expenzify.data.dao.ExpenseDao
import com.example.expenzify.data.model.ExpenseEntity
import com.example.expenzify.data.model.ExpenseSummary
import com.example.expenzify.features.add_expense.AddExpenseScreen
import com.example.expenzify.features.stats.StatsScreen
import com.github.mikephil.charting.data.Entry


class StatsViewModel(expenseDao: ExpenseDao): ViewModel() {
    val topExpenseList = expenseDao.getTopExpensesByType("Expense")
    val topIncomeList = expenseDao.getTopExpensesByType("Income")
    val topTransactionList = expenseDao.getTopTransactions()
    val expenseEntries = expenseDao.getAllExpensesByDate("Expense")
    val incomeEntries = expenseDao.getAllExpensesByDate("Income")

    fun getExpenseEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        return entries.map { Entry(it.date.toFloat(), it.total_amount.toFloat()) }
    }

    fun getIncomeEntriesForChart(entries: List<ExpenseSummary>): List<Entry> {
        return entries.map { Entry(it.date.toFloat(), it.total_amount.toFloat()) }
    }

    fun getBalanceEntriesForChart(expenseEntries: List<ExpenseSummary>, incomeEntries: List<ExpenseSummary>): List<Entry> {
        val balanceEntries = mutableListOf<Entry>()
        val expensesMap = expenseEntries.associateBy { it.date }
        val incomesMap = incomeEntries.associateBy { it.date }

        expensesMap.forEach { (date, expense) ->
            val income = incomesMap[date]?.total_amount ?: 0f
            val balance = income.toDouble() - expense.total_amount
            balanceEntries.add(Entry(date.toFloat(), balance.toFloat()))
        }

        return balanceEntries
    }
}


class StatsViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StatsViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()

            return StatsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
