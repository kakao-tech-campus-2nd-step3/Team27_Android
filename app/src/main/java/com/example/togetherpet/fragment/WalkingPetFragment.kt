package com.example.togetherpet.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.location.Location
import android.opengl.Visibility
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.togetherpet.R
import com.example.togetherpet.databinding.FragmentWalkingPetBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import java.util.Arrays


class WalkingPetFragment : Fragment() {
    private var _binding: FragmentWalkingPetBinding? = null
    private val binding get() = _binding!!
    var kakaoMap : KakaoMap? = null
    lateinit var locationPermissionRequest : ActivityResultLauncher<Array<String>>
    lateinit var fusedLocationClient : FusedLocationProviderClient
    lateinit var locationCallback : LocationCallback
    private val locArray = ArrayList<LatLng>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalkingPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("testt", "시작")
        initVar()
        checkPermission()

    }



    fun drawLine(latLng: LatLng){
        val layer = kakaoMap?.routeLineManager?.layer
        val lineStyle = RouteLineStyle.from(16f, Color.RED)
        lineStyle.strokeColor = Color.BLACK
        val stylesSet = RouteLineStylesSet.from(
            RouteLineStyles.from(lineStyle)
        )
        val segment = RouteLineSegment.from(
            arrayOf(locArray.last(), latLng)
        ).setStyles(stylesSet.getStyles(0))

        val options = RouteLineOptions.from(segment)
            .setStylesSet(stylesSet)

        val routeLine = layer?.addRouteLine(options)
    }

    fun initVar(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        initMap()
        initListener()
        initResultLauncher()
    }

    fun initResultLauncher(){
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                } else -> {
                Toast.makeText(requireContext(), "권한 거절", Toast.LENGTH_SHORT).show()
            }
            }
        }
    }

    fun initListener(){
        binding.walkingStartButton.setOnClickListener{
            showBoard()
            initBoard()
            binding.walkingStartButton.visibility = View.GONE
            startWalkingTracker()
        }
        binding.walkingDisplayBoard.setOnClickListener{
            // 지도의 스와이프을 막기 위해서 생성. 실제로 하는 역할 X
        }
        binding.walkingStopButton.setOnClickListener{

        }
    }

    fun initMap(){
        var loc = LatLng.from(0.0, 0.0)
        checkPermission()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                loc = LatLng.from(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
                locArray.add(loc)
            }


        val map = binding.walkingMapView
        map.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                Log.d("testt", "MapDestroy")
            }

            override fun onMapError(error: Exception) {
                Log.d("testt", error.message.toString())
                //에러 처리
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("testt", "MapReady")
                kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(loc))
                this@WalkingPetFragment.kakaoMap = kakaoMap
            }
        })
    }

    fun startWalkingTracker(){

        checkPermission()

        val locationRequest = LocationRequest.Builder(5000L).apply {
            setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            setMinUpdateIntervalMillis(5000L)
        }.build()


        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("testt", "Latitude: $latitude, Longitude: $longitude")
                    val latLng = LatLng.from(latitude, longitude)
                    kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(latLng))
                    Toast.makeText(requireContext(), latLng.toString(), Toast.LENGTH_SHORT).show()
                    drawLine(latLng)
                    locArray.add(latLng)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
            return
        }
    }

    fun showBoard(){
        binding.walkingDisplayBoard.visibility = View.VISIBLE
        binding.walkingStopButton.visibility = View.VISIBLE
        binding.calorieText.visibility = View.VISIBLE
        binding.calorieValue.visibility = View.VISIBLE
        binding.timeText.visibility = View.VISIBLE
        binding.timeValue.visibility = View.VISIBLE
        binding.distanceText.visibility = View.VISIBLE
        binding.distanceValue.visibility = View.VISIBLE
        binding.walkingGoalText.visibility = View.VISIBLE
    }

    fun initBoard(){
        binding.calorieValue.text = "0"
        binding.timeValue.text = "00:00:00"
        binding.distanceValue.text = "0"
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

