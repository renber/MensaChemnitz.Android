package de.renebergelt.mensachemnitz.services

import java.time.LocalDate

class SaxonyHolidayService : IHolidayService {

    val holidayHelper = HolidayHelper()

    constructor () {

    }

    override fun isHoliday(date: LocalDate): Boolean {
        return holidayHelper.isFeiertag(date, Land.Sachsen)
    }

    override fun getHolidayName(date: LocalDate): String {
        return holidayHelper.getName(date, Land.Sachsen)
    }
}