package com.udacity.project4.utils


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * @return true if Fine location permission is granted.
 */
fun checkLocationPermission(context: Context): Boolean {
    if (
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    return false
}

/**
 * @return true if background access is granted
 */
fun checkBackgroundLocationAccess(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < 29) {
        return true
    }
    if (
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    }
    return false
}