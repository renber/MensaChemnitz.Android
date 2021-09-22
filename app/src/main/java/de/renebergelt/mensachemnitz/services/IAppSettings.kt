package de.renebergelt.mensachemnitz.services

import de.renebergelt.mensachemnitz.types.MensaType

interface IAppSettings {

    val appVersion : String

    val menuCacheFolderName : String

    val imageCacheFolderName : String

    var startupMensa : String

    var lastMensa : MensaType

    var useCache : Boolean

    var nextDayAfterLunch : Boolean

    var veggieMode : Boolean

    fun isNetworkAvailable() : Boolean

    fun clearCache()

    fun removeOutdatedMenuesFromCache()

    fun calculateCacheSize() : Long
}