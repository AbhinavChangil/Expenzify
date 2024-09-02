package com.example.expenzify.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expenzify.data.ExpenseDatabase
import com.example.expenzify.data.dao.ExpenseDao
import com.example.expenzify.data.model.ExpenseEntity
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class AddExpenseViewModel(val context: Context, val dao: ExpenseDao): ViewModel(){

    suspend fun addExpense(expenseEntity: ExpenseEntity): Boolean {
        return try {
            dao.insertExpense(expenseEntity)
            Toast.makeText(context, "Transaction Added Successfully!", Toast.LENGTH_SHORT).show()
            true
        } catch (ex: Throwable) {
            Toast.makeText(context, "Error Adding Transaction: ${ex.message}", Toast.LENGTH_SHORT).show()
            false
        }
    }

}


class AddExpenseViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddExpenseViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()

            return AddExpenseViewModel(context, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}