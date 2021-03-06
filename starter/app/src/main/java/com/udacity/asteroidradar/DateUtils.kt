package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    /**
     * Get Simpledate from String date
     *
     * @param date string
     * @return Simpledate from date string
     */
    private fun getDateString(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

    /**
     * Get current date as formatted string string
     *
     * @return current date string
     */
    fun getCurrentDateString(): String {
        val date = Calendar.getInstance().time
        return getDateString(date)
    }

    /**
     * Get formatted date string for a date x days in the future
     *
     * @param days from now
     * @return formatted date string
     */
    fun getDateStringDaysFromNow(days: Int): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_MONTH, days)
        val date = cal.time
        return getDateString(date)
    }

    /**
     * Get a List with a sequence of dates from today to x days in the future
     *
     * @param days in the future
     * @return list of string days
     */
    fun getDateStringSequence(days: Int): List<String> {
        val dateList = mutableListOf<String>()
        dateList.add(getCurrentDateString())

        for (day in 1 until days) {
            val date = getDateStringDaysFromNow(day)
            dateList.add(date)
        }
        return dateList
    }

    /**
     * Get current date without time using Calendar class
     *
     * @return
     */
    fun getDateWithoutTimeUsingCalendar(): Date {
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            return calendar.time
        }
    }

    /**
     * Check if a string date is today's date, used for filtering asteroid data
     *
     * @param date
     * @return should be kept in list
     */
    fun isDateToday(date: String): Boolean {
        val today = getDateWithoutTimeUsingCalendar()
        val compareDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)
        return today == compareDate
    }

    /**
     * Check if a string date is in the same week as today, used for filtering asteroid data
     *
     * @param date
     * @return should be kept in list
     */
    fun isDateThisWeek(date: String): Boolean {
        val cal = Calendar.getInstance()
        val todayWeek = cal.get(Calendar.WEEK_OF_YEAR)
        val todayYear = cal.get (Calendar.YEAR)

        val compareDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date) ?: return false
        cal.time = compareDate
        val compareWeek = cal.get(Calendar.WEEK_OF_YEAR)
        val compareYear = cal.get (Calendar.YEAR)

        return todayWeek == compareWeek && todayYear == compareYear
    }
}