package de.renebergelt.mensachemnitz.services

import android.content.Context
import android.util.Log
import de.renebergelt.mensachemnitz.types.Dish
import de.renebergelt.mensachemnitz.types.MensaType
import java.io.*
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CachingMenuProvider : IMensaMenuProvider {

    val baseUrl = "https://www.swcz.de/bilderspeiseplan/xml.php?plan=%s&jahr=%d&monat=%d&tag=%d";
    val parser = XmlMenuParser()

    val context : Context
    val settings : IAppSettings

    /*
     *
     * delegatedProvider - the provider to retrieve menues from when
     * requested element is not in the cache
     */
    constructor(context : Context, settings : IAppSettings) {
        this.context = context
        this.settings = settings
    }

    override fun loadDishesForDay(mensa: MensaType, date: LocalDate, forceReload: Boolean): List<Dish> {

        if (!forceReload) {
            val cached = tryGetFromCache(mensa, date)
            if (cached != null) {
                return cached;
            }
        }

        // retrieve the xml from the website
        val url = baseUrl.format(mensa.getXmlId(), date.year, date.month.value, date.dayOfMonth)
        return URL(url).openStream().use { it ->

            // as we cannot reset URL's Stream
            // read it to a memory stream first
            val bufOut = ByteArrayOutputStream()
            it.copyTo(bufOut)

            val bufIn = ByteArrayInputStream(bufOut.toByteArray())
            bufIn.use { buf ->
                val list = parser.parseMenu(buf)
                try {
                    if (list.isNotEmpty()) {
                        buf.reset()
                        storeInCache(mensa, date, buf)
                    }
                } catch (e : Exception) {
                    // caching error
                    Log.e("menuprovider", "Error while caching: " + e.message)
                }

                return list
            }
        }
    }

    fun getCacheFilename(mensa : MensaType, date: LocalDate) : String {
        return mensa.toString() + "_" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xml"
    }

    fun tryGetFromCache(mensa : MensaType, date: LocalDate) : List<Dish>? {

        if (!settings.useCache) return null

        val dir = context.cacheDir.resolve(settings.menuCacheFolderName)

        if (!dir.exists())
            return null

        try {
            val f = dir.resolve(getCacheFilename(mensa, date))
            if (f.exists()) {
                FileInputStream(f).use { it ->
                    return parser.parseMenu(it)
                }
            }
        } catch (e : Exception) {
            Log.e("menuprovider.tryGetFromCache", e.message)
        }

        // not in cache
        return null
    }

    /**
     * We directly store the retrieved XML file
     * so that we do not need two independend serialization/deserialization routines
     */
    fun storeInCache(mensa : MensaType, date: LocalDate, stream: InputStream) {

        if (!settings.useCache) return

        try {
            val dir = context.cacheDir.resolve(settings.menuCacheFolderName)

            if (!dir.exists()) {
                dir.mkdirs()
            }

            val fname = getCacheFilename(mensa, date)

            FileOutputStream(dir.resolve(fname)).use { it ->
                stream.copyTo(it)
            }
        } catch (e : Exception) {
            // caching error
            Log.e("menuprovider.storeInCache", e.message)
        }
    }
}