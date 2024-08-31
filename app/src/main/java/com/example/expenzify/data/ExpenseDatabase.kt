package com.example.expenzify.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expenzify.data.dao.ExpenseDao
import com.example.expenzify.data.model.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpenseDatabase: RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao


    companion object{
        const val DATABASE_NAME = "expense_database"

        @JvmStatic
        fun getDatabase(context: Context) : ExpenseDatabase{
            return Room.databaseBuilder(
                context,
                ExpenseDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object: RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    initBasicData(context)
                }

                fun initBasicData(context: Context){
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = getDatabase(context).expenseDao()
                        dao.insertExpense(ExpenseEntity(1, "Income", "Salary", "Salary", 5000.03, System.currentTimeMillis()))
                        dao.insertExpense(ExpenseEntity(2, "Expense", "Groceries", "Food", 200.04, System.currentTimeMillis()))
                        dao.insertExpense(ExpenseEntity(3, "Income", "Freelance", "Work", 1000.00, System.currentTimeMillis()))
                        dao.insertExpense(ExpenseEntity(4, "Expense", "Rent", "House", 1000.64, System.currentTimeMillis()))
                        dao.insertExpense(ExpenseEntity(5, "Income", "Bonus", "Work", 500.84, System.currentTimeMillis()))
                        dao.insertExpense(ExpenseEntity(6, "Expense", "Transportation", "Transport", 300.04, System.currentTimeMillis()))
                        dao.insertExpense(ExpenseEntity(7, "Income", "Dividends", "Work", 200.01, System.currentTimeMillis()))
                        dao.insertExpense(ExpenseEntity(8, "Expense", "Entertainment", "Fun", 100.02, System.currentTimeMillis()))
                        dao.insertExpense(ExpenseEntity(9, "Income", "Investment", "Work", 1000.00, System.currentTimeMillis()))
                    }
                }
            })

                .build()
        }
    }
}