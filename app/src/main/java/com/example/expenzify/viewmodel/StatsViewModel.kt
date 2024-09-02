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
    val expenseEntries = expenseDao.getAllExpensesByDate("Expense")

    fun getEntriesForChart(entries: List<ExpenseSummary>):List<Entry> {

        val listOfEntries = mutableListOf<Entry>()
        for(entry in entries) {
            val formattedDate = entry.date
            listOfEntries.add(Entry(formattedDate.toFloat(), entry.total_amount.toFloat()))
        }
        return listOfEntries
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
