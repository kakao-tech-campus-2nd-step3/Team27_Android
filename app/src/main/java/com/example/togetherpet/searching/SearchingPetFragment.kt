package com.example.togetherpet.searching

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.togetherpet.databinding.FragmentSearchingPetBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import java.lang.Exception

class SearchingPetFragment : Fragment() {
    private var _binding: FragmentSearchingPetBinding? = null
    private val binding get() = _binding!!

    //Google Play 위치 API
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    private var kakaoMap: KakaoMap? = null

    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchingPetBinding.inflate(inflater, container, false)

        //FusedLocationProviderClient 등록
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // 권한 요청 초기화
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}

            override fun onMapError(p0: Exception?) {}

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
                kakaoMap = p0
                checkLocationPermission()
            }

            override fun getPosition(): LatLng {
                return super.getPosition()
            }
        })

        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        // 권한 허용 여부 확인
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            //권한 o -> 위치 정보 가져오기
            getCurrentLocation()
        } else {
            //권한 x -> 권한 요청
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                latitude = it.latitude
                longitude = it.longitude
                Log.d("yeong", "현재 위치: $latitude / $longitude")
                displayLocation(latitude,longitude)
            }
    }

    private fun displayLocation(latitude: Double, longitude: Double) {
        val position = LatLng.from(latitude, longitude)
        kakaoMap?.moveCamera(
            CameraUpdateFactory.newCenterPosition(position)
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }
}
