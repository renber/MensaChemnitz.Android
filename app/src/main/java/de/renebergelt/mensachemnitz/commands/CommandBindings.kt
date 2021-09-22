package de.renebergelt.mensachemnitz.commands

import android.content.ContextWrapper
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

@BindingAdapter("command")
fun View.bindCommand(command: ICommand) {

    val l = getLifeCycleOwner(this)

    command.canExecute.observe(l, Observer {
        it?.let {
            this.isEnabled = it
        }
    })

    // execute command on click
    this.setOnClickListener { command.execute() }

    this.isEnabled = command.canExecute()
}

fun getLifeCycleOwner(view: View) : LifecycleOwner {
    var context = view.context
    while (!(context is LifecycleOwner)) {
        context = (context as ContextWrapper).baseContext
    }

    return context as LifecycleOwner
}