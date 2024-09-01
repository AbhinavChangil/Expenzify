package com.example.expenzify

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {

    fun dateFormatToHumanReadableFormat(dateInMillis: Long) : String{
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return dateFormat.format(dateInMillis)
    }
}