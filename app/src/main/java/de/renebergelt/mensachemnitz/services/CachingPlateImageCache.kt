package de.renebergelt.mensachemnitz.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

class CachingPlateImageCache : IImageProvider {

    val context : Context
    val settings : IAppSettings

    constructor(context : Context, settings : IAppSettings) {
        this.context = context
        this.settings = settings
    }

    fun tryGetFromCache(id: String): Bitmap? {
        if (!settings.useCache || id == "-1")
            return null

        val dir = context.cacheDir.resolve(settings.imageCacheFolderName)

        if (!dir.exists())
            return null

        val fname = dir.resolve(id + ".png")
        if (fname.exists()) {
            try {
                return BitmapFactory.decodeFile(fname.absolutePath)
            } catch (e : Exception) {
                Log.e("tryGetFromCache", "Error while reading image from cache: " + e.message)
            }
        }

        return null
    }

    fun storeInCache(id : String, image : Bitmap) {
        try {
            if (!settings.useCache || id == "-1")
                return

            val dir = context.cacheDir.resolve(settings.imageCacheFolderName)

            if (!dir.exists())
                dir.mkdirs()

            val f = dir.resolve(id + ".png")
            FileOutputStream(f).use { it -> image.compress(Bitmap.CompressFormat.PNG, 100, it) }
        } catch (e : Exception) {
            Log.e("storeInCache", "Error while writing image to cache: " + e.message)
        }
    }

    fun downloadImage(url : String) : Bitmap? {
        var bm: Bitmap? = null
        try {
            val aURL = URL(url)
            val conn: URLConnection = aURL.openConnection()
            conn.connect()
            val inStream: InputStream = conn.getInputStream()
            val bStream = BufferedInputStream(inStream)
            bm = BitmapFactory.decodeStream(bStream)
            bStream.close()
            inStream.close()

            return bm
        } catch (e: Exception) {
            Log.e("downloadImage", "Error downloading image: " + e.message)
            return null
        }
    }

    override fun getImage(id: String, url: String): Bitmap? {

        val cacheBmp = tryGetFromCache(id)

        if (cacheBmp != null) {
            return cacheBmp
        }

        val bmp = downloadImage(url)
        if (bmp != null) {
            storeInCache(id, bmp)
        }

        return bmp
    }
}