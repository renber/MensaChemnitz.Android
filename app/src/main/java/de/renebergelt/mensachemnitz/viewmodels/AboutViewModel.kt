package de.renebergelt.mensachemnitz.viewmodels

import androidx.lifecycle.ViewModel
import de.renebergelt.mensachemnitz.services.IAppSettings

class AboutViewModel : ViewModel {

    val appSettings : IAppSettings

    val version : String
        get() { return appSettings.appVersion }

    constructor(appSettings : IAppSettings) {
        this.appSettings = appSettings
    }

}