package de.renebergelt.mensachemnitz.commands

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RelayCommand : ICommand {
    val _canExecute = MutableLiveData<Boolean>()

    override val canExecute: LiveData<Boolean> = _canExecute

    var executeFunc: () -> Unit = {}
    var canExecuteFunc: () -> Boolean = { true }

    constructor (onExecute: () -> Unit)
            : this(onExecute, {true }){
        // --
    }

    constructor (onExecute: () -> Unit, canExecute: () -> Boolean) {
        executeFunc = onExecute
        canExecuteFunc = canExecute

        updateCanExecute()
    }

    override fun execute() {
        if (canExecuteFunc()) {
            executeFunc()

            updateCanExecute()
        }
    }

    override fun canExecute(): Boolean {
        return canExecute.value!!
    }

    override fun updateCanExecute() {
        _canExecute.value = canExecuteFunc()
    }
}