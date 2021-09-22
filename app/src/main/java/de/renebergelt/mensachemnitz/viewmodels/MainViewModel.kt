package de.renebergelt.mensachemnitz.viewmodels

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.renebergelt.mensachemnitz.R
import de.renebergelt.mensachemnitz.commands.ICommand
import de.renebergelt.mensachemnitz.commands.RelayCommand
import de.renebergelt.mensachemnitz.services.*
import de.renebergelt.mensachemnitz.types.MensaType
import me.tatarka.bindingcollectionadapter2.BR
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime


class MainViewModel : ViewModel {

    private val holidayService : IHolidayService
    private val viewModelFactory : IViewModelFactory
    private val settings : IAppSettings

    val lang = LanguageDictionary()

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _isCafeteria = MutableLiveData<Boolean>()
    val isCafeteria = _isCafeteria

    val days : ObservableList<DayViewModel> = ObservableArrayList<DayViewModel>()
    val dayItemBinding = ItemBinding.of<DayViewModel>(BR.viewModel, R.layout.day_fragment)

    val selectedDay = MutableLiveData<DayViewModel?>()

    var currentMensa : MensaType
            set(value) {
                field = value
                _isCafeteria.value = (value == MensaType.CafeteriaStraNa || value == MensaType.CafeteriaReichenhain)
                updateTitle()
            }

    val switchLocationCommand : ICommand = RelayCommand( ::switchLocation );
    val switchMensaTypeCommand : ICommand = RelayCommand ( ::switchMensaType )
    val refreshCommand : ICommand = RelayCommand ( ::refresh )

    constructor(viewModelFactory : IViewModelFactory, settings : IAppSettings, holidayService: IHolidayService) : super() {
        this.viewModelFactory = viewModelFactory
        this.settings = settings
        this.holidayService = holidayService

        // start-up Mensa from settings
        try {
            val startup = settings.startupMensa
            if (startup == "lastMensa") {
                currentMensa = settings.lastMensa
            } else {
                currentMensa = MensaType.valueOf(startup)
            }
        } catch (e :Exception) {
            currentMensa = MensaType.MensaReichenhain
        }

        loadMensa(currentMensa, true)

        selectedDay.observeForever( { d -> d?.load(false) } )
    }

    fun updateCommands() {
        // update commands which have canExecute conditions
    }

    private fun switchLocation() {
        var newMensa : MensaType
        when (currentMensa) {
            MensaType.MensaStraNa -> newMensa = MensaType.MensaReichenhain
            MensaType.CafeteriaStraNa -> newMensa = MensaType.CafeteriaReichenhain
            MensaType.MensaReichenhain -> newMensa = MensaType.MensaStraNa
            MensaType.CafeteriaReichenhain -> newMensa = MensaType.CafeteriaStraNa
        }

        loadMensa(newMensa, false)
    }

    private fun switchMensaType() {
       var newMensa : MensaType
       when (currentMensa) {
            MensaType.MensaStraNa -> newMensa = MensaType.CafeteriaStraNa
            MensaType.CafeteriaStraNa -> newMensa = MensaType.MensaStraNa
            MensaType.MensaReichenhain -> newMensa = MensaType.CafeteriaReichenhain
            MensaType.CafeteriaReichenhain -> newMensa = MensaType.MensaReichenhain
       }

       loadMensa(newMensa, false)
    }

    private fun refresh() {
        selectedDay.value?.load(true)
    }

    private fun loadMensa(mensa: MensaType, canAdjustSelectedDay: Boolean) {

        // save the current mensa
        settings.lastMensa = mensa

        val newDays = ArrayList<DayViewModel>()

        var date = LocalDate.now()
        while (newDays.size < 5) {
            // skip the weekend
            if (date.dayOfWeek != DayOfWeek.SATURDAY && date.dayOfWeek != DayOfWeek.SUNDAY) {

                if (holidayService.isHoliday(date)) {
                    val holidayVm = viewModelFactory.createHolidayViewModel(lang["holiday:" + holidayService.getHolidayName(date)], date)
                    newDays.add(holidayVm)
                } else {
                    val dayVm = viewModelFactory.createDayViewModel(mensa, date)
                    newDays.add(dayVm)
                }
            }

            date = date.plusDays(1)
        }

        days.clear()
        days.addAll(newDays)

        if (canAdjustSelectedDay) {
            adjustSelectedDay()
        }

        currentMensa = mensa
    }

    fun adjustSelectedDay() {
        // show the next day if the first item is the day and it is past 2 p.m.
        if (settings.nextDayAfterLunch && LocalTime.now().hour >= 14 && days[0].date == LocalDate.now()) {
            selectedDay.value = days[1];
        }
    }

    private fun updateTitle() {
        _title.value = currentMensa.getHumanFriendlyName()
    }
}
