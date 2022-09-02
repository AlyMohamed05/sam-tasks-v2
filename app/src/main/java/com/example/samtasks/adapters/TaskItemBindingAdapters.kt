package com.example.samtasks.adapters

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.gms.maps.model.LatLng

@BindingAdapter("content")
fun TextView.content(title: String?) {
    text = title ?: ""
}

@BindingAdapter("finished")
fun CheckBox.finished(finished: Boolean) {
    isChecked = finished
}

@BindingAdapter("showLocationIcon")
fun ImageView.showLocationIcon(location: LatLng?) {
    val visible = location != null
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}