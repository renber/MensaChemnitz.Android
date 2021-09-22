package de.renebergelt.mensachemnitz.viewmodels

import androidx.annotation.UiThread
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.renebergelt.mensachemnitz.R
import de.renebergelt.mensachemnitz.services.IAppSettings
import de.renebergelt.mensachemnitz.services.IImageProvider
import de.renebergelt.mensachemnitz.services.IMensaMenuProvider
import de.renebergelt.mensachemnitz.services.IViewModelFactory
import de.renebergelt.mensachemnitz.types.Dish
import de.renebergelt.mensachemnitz.types.MensaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.BR
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

/**
 * Contains a menu for a given day and mensa
 */
class DayViewModel : ViewModel {

    val settings : IAppSettings?
    val viewModelFactory : IViewModelFactory?
    val menuProvider : IMensaMenuProvider?

    val mensa : MensaType
    val date : LocalDate

    var wasLoaded = false

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    val _hasNoMenu = MutableLiveData<Boolean>()
    val hasNoMenu : LiveData<Boolean> = _hasNoMenu

    val dateTitle : String
        get() {
            when(date) {
                LocalDate.now() -> return "Heute"
                LocalDate.now().plusDays(1) -> return "Morgen"
                else -> return date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.GERMANY)
            }
        }

    val dateText : String
        get () { return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) }

    val dishes : ObservableList<DishViewModel> = ObservableArrayList<DishViewModel>()
    val dishItemBinding = ItemBinding.of<DishViewModel>(BR.viewModel, R.layout.dish_fragment)

    val isHoliday : Boolean
        get() { return field }

    val holidayName : String
        get() { return field }

    constructor(mensa : MensaType, date: LocalDate, menuProvider : IMensaMenuProvider, viewModelFactory : IViewModelFactory, settings : IAppSettings) {
        this.mensa = mensa
        this.date = date

        this.menuProvider = menuProvider
        this.viewModelFactory = viewModelFactory
        this.settings = settings

        this.isHoliday = false
        this.holidayName = ""
    }

    constructor(holidayName : String, date : LocalDate) {
        this.mensa = MensaType.MensaStraNa
        this.date = date

        this.menuProvider = null
        this.viewModelFactory = null
        this.settings = null

        this.isHoliday = true
        this.holidayName = holidayName
    }

    fun load(force : Boolean) {
        if (isHoliday || settings == null || menuProvider == null || viewModelFactory == null)
            return

        if (force || !wasLoaded) {
            viewModelScope.launch {
                dishes.clear()

                try {
                    _isLoading.value = true

                    val lst = loadAsync()

                    if (lst.size == 0) {
                        _hasNoMenu.value = true
                    } else {
                        _hasNoMenu.value = false

                        val dlst =
                            lst.filter { d -> !settings.veggieMode || d.ingredients.vegetarian }

                        dishes.clear()
                        dishes.addAll(dlst.mapIndexed { idx, dish ->
                            viewModelFactory.createDishViewModel(dish, idx == lst.size - 1)
                        })
                    }
                } catch (e : Exception) {
                    _hasNoMenu.value = true
                } finally {
                    _isLoading.value = false
                    wasLoaded = true
                }
            }
        }
    }

    @UiThread
    suspend fun loadAsync() : List<Dish> = withContext(Dispatchers.Default)  {
        menuProvider?.loadDishesForDay(mensa, date, false) ?: ArrayList<Dish>(0)
    }

}