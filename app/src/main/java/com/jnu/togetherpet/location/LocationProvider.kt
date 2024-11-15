package com.jnu.togetherpet.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class LocationProvider(
    private val context: Context,
    private val onLocationResult: (latitude: Double, longitude: Double) -> Unit
) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                val latitude = it.latitude
                val longitude = it.longitude
                Log.d("LocationProvider", "현재 위치: $latitude / $longitude")
                onLocationResult(latitude, longitude)
            }
            .addOnFailureListener {
                Log.e("LocationProvider", "위치 정보를 가져오는데 실패했습니다.", it)
            }
    }
}