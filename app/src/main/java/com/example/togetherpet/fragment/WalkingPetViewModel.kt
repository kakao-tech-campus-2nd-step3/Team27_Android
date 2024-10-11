package com.example.togetherpet.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class WalkingPetViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) :
    ViewModel() {

    private val _distance = MutableStateFlow<Int>(0)
    private val _arrayLastTwoLoc = MutableStateFlow<ArrayList<LatLng>>(ArrayList())
    private val _calories = MutableStateFlow<Int>(0)
    private val _time = MutableStateFlow<Long>(0)
    private var base: Long = 0
    val distance: StateFlow<Int> get() = _distance.asStateFlow()
    val arrayLastTwoLoc: StateFlow<ArrayList<LatLng>> get() = _arrayLastTwoLoc.asStateFlow()
    val calories: StateFlow<Int> get() = _calories.asStateFlow()
    val time: StateFlow<Long> get() = _time.asStateFlow()

    private val arrayLoc: ArrayList<LatLng> = ArrayList<LatLng>()
    private lateinit var locationCallback : LocationCallback


    fun calculateDistance(latLng1: LatLng, latLng2: LatLng) {
        val R = 6372.8 * 1000
        val dLat = Math.toRadians(latLng1.latitude - latLng2.latitude)
        val dLon = Math.toRadians(latLng1.longitude - latLng2.longitude)
        val a =
            sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(latLng1.latitude)) * cos(
                Math.toRadians(latLng2.latitude)
            )
        val c = 2 * asin(sqrt(a))
        _distance.value += (R * c).toInt()
        Log.d("testt", _distance.value.toString())
    }

    fun calculateCalories() {

        val timeHours = time.value / (1000.0 * 60 * 60)
        // MET, 평균체중 (MVP)
        val MET = 4.0
        val averageWeightKg = 15.0

        _calories.value = (MET * averageWeightKg * timeHours).toInt()
    }

    fun setTimeBase() {
        base = SystemClock.elapsedRealtime()
    }

    fun timerStart() {
        _time.value = SystemClock.elapsedRealtime() - base
        Log.d("testt", "$time = {time}")
    }

    fun initLocationTracking() {
        val serviceIntent = Intent(context, LocationService::class.java)
        Log.d("testt", "service 호출")
        context.startForegroundService(serviceIntent)
    }

    fun startLocationTracking() {
        initLocationTracking()
        startLocationUpdate()
    }

    fun stopLocationTracking() {
        Intent(context, LocationService::class.java).apply(context::stopService)
        fusedLocationProviderClient.removeLocationUpdates(
            locationCallback
        )

    }

    fun setLocationCallback(): LocationCallback {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("testt", "Latitude: $latitude, Longitude: $longitude")
                    val latLng = LatLng.from(latitude, longitude)
                    arrayLoc.add(latLng)
                    Log.d("testt", arrayLoc.toString())
                    updateLastTwoLocation(latLng)
                    calculateBetweenLastTwoLocation()
                    calculateCalories()
                    Log.d("testt", arrayLastTwoLoc.value.toString())
                }
            }
        }
        return locationCallback
    }

    fun updateLastTwoLocation(latLng: LatLng) {
        if (_arrayLastTwoLoc.value.size >= 2) {
            val newArrayList = ArrayList(_arrayLastTwoLoc.value).apply {
                add(latLng)
                removeAt(0)
            }
            _arrayLastTwoLoc.value = newArrayList
        } else {
            val newArrayList = ArrayList(_arrayLastTwoLoc.value).apply {
                add(latLng)
            }
            _arrayLastTwoLoc.value = newArrayList
        }
    }

    fun calculateBetweenLastTwoLocation() {
        calculateDistance(_arrayLastTwoLoc.value.first(), _arrayLastTwoLoc.value.last())
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdate() {
        val locationCallback = setLocationCallback()
        val locationRequest = LocationRequest.Builder(5000L).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            setMinUpdateIntervalMillis(5000L)
        }.build()
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
//        locationService.startLocationUpdate(locationCallback)
    }
}