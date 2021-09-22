package de.renebergelt.mensachemnitz

import de.renebergelt.mensachemnitz.services.HolidayHelper
import de.renebergelt.mensachemnitz.services.Land
import org.junit.Test
import java.time.LocalDate
import org.junit.Assert.*

class HolidayHelperTest {

    @Test
    fun test_holidays2019() {
        val helper = HolidayHelper()

        assertTrue(helper.isFeiertag(LocalDate.of(2019, 1, 1), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 4, 19), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 4, 22), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 5, 1), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 5, 30), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 6, 10), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 10, 3), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 10, 31), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 11, 20), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 12, 25), Land.Sachsen))
        assertTrue(helper.isFeiertag(LocalDate.of(2019, 12, 26), Land.Sachsen))

    }

}