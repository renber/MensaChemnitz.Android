package de.renebergelt.mensachemnitz.services

import android.graphics.Bitmap

interface IImageProvider {

    fun getImage(id : String, url : String) : Bitmap?

}