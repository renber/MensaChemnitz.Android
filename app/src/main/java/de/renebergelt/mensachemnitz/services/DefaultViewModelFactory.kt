package de.renebergelt.mensachemnitz.services

import de.renebergelt.mensachemnitz.types.Dish
import de.renebergelt.mensachemnitz.types.MensaType
import de.renebergelt.mensachemnitz.viewmodels.DayViewModel
import de.renebergelt.mensachemnitz.viewmodels.DishViewModel
import org.koin.core.Koin
import org.koin.core.KoinComponent
import java.time.LocalDate

class DefaultViewModelFactory : IViewModelFactory, KoinComponent {

    private val container : Koin = getKoin()

    override fun createDayViewModel(mensa: MensaType, date: LocalDate) : DayViewModel {
        return DayViewModel(mensa, date, container.get(), container.get(), container.get())
    }

    override fun createHolidayViewModel(holidayName : String, date : LocalDate) : DayViewModel {
        return DayViewModel(holidayName, date)
    }

    override fun createDishViewModel(model: Dish, isLastItem: Boolean) : DishViewModel {
        return DishViewModel(model, isLastItem, container.get())
    }
}