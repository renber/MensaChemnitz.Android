package de.renebergelt.mensachemnitz.services

import androidx.lifecycle.LifecycleOwner
import de.renebergelt.mensachemnitz.types.Dish
import de.renebergelt.mensachemnitz.types.MensaType
import de.renebergelt.mensachemnitz.viewmodels.DayViewModel
import de.renebergelt.mensachemnitz.viewmodels.DishViewModel
import java.time.LocalDate

interface IViewModelFactory {

    fun createDayViewModel(mensa : MensaType, date : LocalDate) : DayViewModel

    fun createHolidayViewModel(holidayName : String, date : LocalDate) : DayViewModel

    fun createDishViewModel(model : Dish, isLastItem : Boolean) : DishViewModel

}