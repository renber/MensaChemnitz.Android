package de.renebergelt.mensachemnitz.services

import java.time.LocalDate

interface IHolidayService {

    fun isHoliday(date : LocalDate) : Boolean

    fun getHolidayName(date : LocalDate) : String

}