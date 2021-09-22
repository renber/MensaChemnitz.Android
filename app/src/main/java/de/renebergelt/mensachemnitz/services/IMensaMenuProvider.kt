package de.renebergelt.mensachemnitz.services

import de.renebergelt.mensachemnitz.types.Dish
import de.renebergelt.mensachemnitz.types.MensaType
import java.time.LocalDate

interface IMensaMenuProvider {

    fun loadDishesForDay(mensa : MensaType, date: LocalDate, forceReload : Boolean) : List<Dish>

}