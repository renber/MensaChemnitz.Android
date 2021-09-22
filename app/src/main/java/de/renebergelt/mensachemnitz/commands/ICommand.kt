package de.renebergelt.mensachemnitz.commands

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface ICommand {

    val canExecute : LiveData<Boolean>

    fun execute()

    fun updateCanExecute()

    fun canExecute() : Boolean

}