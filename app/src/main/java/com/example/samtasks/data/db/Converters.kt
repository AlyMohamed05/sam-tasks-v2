package com.example.samtasks.data.db

import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class Converters {

    private val converter = Gson()

    @TypeConverter
    fun fromLatLngToString(location: LatLng?): String? {
        if (location == null) {
            return null
        }
        return converter.toJson(location)
    }

    @TypeConverter
    fun fromJsonToLatLng(json: String?): LatLng? {
        if (json == null) {
            return null
        }
        return converter.fromJson(json, LatLng::class.java)
    }
}