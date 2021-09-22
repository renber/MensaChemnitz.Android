package de.renebergelt.mensachemnitz.services

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import de.renebergelt.mensachemnitz.types.MensaType
import java.io.File

/**
 * App settings implemented using Android's shared preferences
 */
class SharedPreferencesAppSettings : IAppSettings {

    private val context : Context
    private val preferences : SharedPreferences

    override val menuCacheFolderName : String
        get() = "menuCache"

    override val imageCacheFolderName : String
        get() = "imageCache"

    override val appVersion: String
        get() = "1.0.0 für Android"

    override var startupMensa: String
        get() = preferences.getString("startupMensa", "lastmensa") !!
        set(value) {preferences.edit().putString("startupMensa", value).apply()}

    override var lastMensa: MensaType
        get() = MensaType.valueOf(preferences.getString("lastMensa", MensaType.MensaReichenhain.toString()) !!)
        set(value) = preferences.edit().putString("lastMensa", value.toString()).apply()

    override var useCache: Boolean
        get() = preferences.getBoolean("useCache", true)
        set(value) = preferences.edit().putBoolean("useCache", value).apply()

    override var nextDayAfterLunch: Boolean
        get() = preferences.getBoolean("nextDayAfterLunch", true)
        set(value) = preferences.edit().putBoolean("nextDayAfterLunch", value).apply()

    override var veggieMode: Boolean
        get() = preferences.getBoolean("veggieMode", false)
        set(value) = preferences.edit().putBoolean("veggieMode", value).apply()

    override fun isNetworkAvailable() : Boolean {
        return true
    }

    constructor(context : Context) {
        this.context = context
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun clearCache() {
        clearCacheFolder(context.cacheDir.resolve(menuCacheFolderName));
        clearCacheFolder(context.cacheDir.resolve(imageCacheFolderName));
    }

    override fun removeOutdatedMenuesFromCache() {

    }

    override fun calculateCacheSize() : Long {
        return calculateFolderSize(context.cacheDir.resolve(menuCacheFolderName)) +
               calculateFolderSize(context.cacheDir.resolve(imageCacheFolderName));

    }

    private fun calculateFolderSize(folder : File) : Long {
        if (!folder.exists() || !folder.isDirectory)
            return 0

        var totalSize : Long = 0

        for(f in folder.listFiles()) {
            totalSize += f.length()
        }

        return totalSize
    }

    private fun clearCacheFolder(folder : File) {
        if (folder.exists()) {
            folder.deleteRecursively()
        }
    }
}