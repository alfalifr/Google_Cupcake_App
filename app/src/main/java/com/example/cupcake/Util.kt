package com.example.cupcake

import java.text.SimpleDateFormat
import java.util.*

object Util {
  fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    return sdf.format(date)
  }

  fun get5DateFurther(): List<String> {
    val dates = mutableListOf<String>()
    val cal = Calendar.getInstance()
    repeat(5) {
      dates += formatDate(cal.time)
      cal.add(Calendar.DATE, 1)
    }
    return dates
  }

  fun isDateNow(formatted: String): Boolean =
    formatted.equals(
      formatDate(Calendar.getInstance().time),
      ignoreCase = true
    )
}