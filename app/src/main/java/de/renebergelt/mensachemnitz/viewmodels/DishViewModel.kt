package de.renebergelt.mensachemnitz.viewmodels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.renebergelt.mensachemnitz.services.IImageProvider
import de.renebergelt.mensachemnitz.types.Dish
import de.renebergelt.mensachemnitz.types.DishIngredients
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection


/***
 * Represents a dish from a menu
 */
class DishViewModel : ViewModel {

    val imageProvider : IImageProvider

    val model : Dish

    val id : String
        get() { return model.id }

    val name : String
        get() { return model.name }

    val description : String
        get() { return model.description }

    val ingredients : DishIngredients
        get() { return model.ingredients }

    val _image = MutableLiveData<Bitmap>()
    val image : LiveData<Bitmap> = _image

    val hasImage : Boolean
        get() { return model.imageUri != null }

    val priceText : String
        get() { return model.prices.joinToString(" / ") { it -> it.group + ": " + it.value.replace('.', ',') + " €" } }

    val isLastItem : Boolean

    constructor(model : Dish, isLastItem : Boolean, imageProvider : IImageProvider) {
        this.model = model
        this.isLastItem = isLastItem
        this.imageProvider = imageProvider

        loadImage()
    }

    private fun loadImage() {
        if (model.imageUri != null) {
            viewModelScope.launch {
                val bm = retrieveImage(model.id, model.imageUri)
                _image.value = bm
            }
        }
    }

    private suspend fun retrieveImage(id : String, url : String) : Bitmap?  = withContext(Dispatchers.Default) {
        imageProvider.getImage(id, url)
    }

}