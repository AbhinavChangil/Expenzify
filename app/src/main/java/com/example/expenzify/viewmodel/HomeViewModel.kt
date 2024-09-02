package com.example.expenzify.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expenzify.R
import com.example.expenzify.data.ExpenseDatabase
import com.example.expenzify.data.dao.ExpenseDao
import com.example.expenzify.data.model.ExpenseEntity

class HomeViewModel(dao: ExpenseDao) : ViewModel() {
    val expenses = dao.getAllExpenses()

    fun getBalance(list: List<ExpenseEntity>) : String {
        var total = 0.00
        list.forEach {
            if(it.type == "Income"){
                total += it.amount
            } else {
                total -= it.amount
            }
        }
        if(total < 0){
            return "- ₹${"%.2f".format(total)}"
        }else{
            return "₹${"%.2f".format(total)}"
        }
    }

    fun getTotalExpenses(list: List<ExpenseEntity>) : String{
        var total = 0.00
        list.forEach {
            if(it.type == "Expense"){
                total += it.amount
            }
        }
        return "₹${"%.2f".format(total)}"
    }

    fun getTotalIncome(list: List<ExpenseEntity>) : String{
        var total = 0.00
        list.forEach {
            if(it.type == "Income"){
                total += it.amount
            }
        }
        return "₹${"%.2f".format(total)}"
    }

    fun getItemIcon(item: ExpenseEntity) : Int{
        if(item.category == "Salary"){
            return R.drawable.ic_netflix
        } else if(item.category == "Freelance"){
            return R.drawable.ic_paypal
        } else if(item.category == "Work"){
            return R.drawable.ic_upwork
        } else if(item.category == "Netflix"){
            return R.drawable.ic_netflix
        } else if(item.category == "Transport"){
            return R.drawable.ic_female
        }
        return R.drawable.ic_starbucks
    }
}

/*
The HomeViewModelFactory class is used to create an instance of HomeViewModel with
specific dependencies. This is necessary because HomeViewModel requires an
ExpenseDao object to interact with the database, and you cannot directly pass this
dependency into the ViewModel when using the default ViewModelProvider in Android.
 */

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()

            return HomeViewModel(dao) as T
        }
         throw IllegalArgumentException("Unknown ViewModel Class")
    }
}