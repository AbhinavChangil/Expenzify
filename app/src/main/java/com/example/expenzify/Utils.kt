package com.example.expenzify

import com.example.expenzify.data.model.ExpenseEntity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun dateFormatToHumanReadableFormat(dateInMillis: Long) : String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return dateFormat.format(dateInMillis)
    }

    fun formatDateForChart(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd-MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatDayMonthYear(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatDayMonth(dateInMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MMM", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }

    fun formatToDecimalValue(d: Double): String {
        return String.format("%.2f", d)
    }

    fun formatStringDateToMonthDayYear(date: Long): String {
        val millis = getMillisFromDate(date)
        return formatDayMonthYear(millis)
    }

    fun getMillisFromDate(date: Long): Long {
        return getMilliFromDate(date)
    }

    fun getMilliFromDate(dateFormat: Long?): Long {
        var date = Date()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try {
            date = formatter.parse(dateFormat.toString())!!
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        println("Today is $date")
        return date.time
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