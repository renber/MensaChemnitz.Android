package de.renebergelt.mensachemnitz.bindings

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import de.renebergelt.mensachemnitz.R
import java.security.AccessController.getContext


@BindingAdapter("android:src")
fun setImageResource(button: com.google.android.material.floatingactionbutton.FloatingActionButton, resource: Int) {
    button.setImageDrawable(ContextCompat.getDrawable(button.getContext(), resource));
}

@BindingAdapter("plateImageSource")
fun setImageResource(imgView: ImageView, bitmap: Bitmap?) {
    if (bitmap == null) {
        // use empty plate
        imgView.setImageResource(R.drawable.plate)
    } else {
        imgView.setImageBitmap(bitmap)
    }
}