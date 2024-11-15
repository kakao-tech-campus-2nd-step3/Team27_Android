package com.jnu.togetherpet.ui.fragment.common

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.Priority
import com.kakao.vectormap.LatLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService() : Service() {


    private val channelId = "location_channel_id"
    private lateinit var locationCallback: LocationCallback
    private val locArray: ArrayList<LatLng> = ArrayList()

    @Inject
    lateinit var fusedLocationClient : FusedLocationProviderClient

    private val locationRequest = LocationRequest.Builder(5000L).apply {
        setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        setMinUpdateIntervalMillis(5000L)
    }.build()


    // 배포를 위해 GPS 기능을 통해 얻은 위치 정보를 데이터베이스에 저장하고 활용하도록 변경
    @SuppressLint("MissingPermission")
    fun startLocationUpdate(locationCallback: LocationCallback) {
        this.locationCallback = locationCallback
        Log.d("testt", "주입")
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdate(){
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("testt", "service 시작")
        Log.d("testt", fusedLocationClient.toString())
        createNotificationChannel()
        startForeground(1, createNotification("산책 중입니다..."))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("testt", "onstratcommand")
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
            val channel = NotificationChannel(
                channelId,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
    }

    private fun createNotification(contentText: String): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("TogetherPet 산책중...")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .build()
    }
}

