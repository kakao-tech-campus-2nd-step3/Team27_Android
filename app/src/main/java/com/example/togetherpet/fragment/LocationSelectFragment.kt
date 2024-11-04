package com.example.togetherpet.searching.report.view

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.togetherpet.data.repository.KakaoLocalRepository
import com.example.togetherpet.databinding.FragmentLocationSelectBinding
import com.example.togetherpet.searching.report.extensions.LocationProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

//24-11-03 Fragment -> DialogFragment 변경
@AndroidEntryPoint
class LocationSelectFragment : DialogFragment() {
    @Inject
    // 나중에 viewmodel과 연결해서 사용하시면 될거같습니다.
    lateinit var kakaoLocalRepository: KakaoLocalRepository

    private var _binding: FragmentLocationSelectBinding? = null
    private val binding get() = _binding!!
    var kakaoMap: KakaoMap? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationProvider: LocationProvider
    private var currentLocation: LatLng = LatLng.from(37.0, 131.0)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationSelectBinding.inflate(inflater, container, false)

        binding.confirmButton.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // LocationProvider 초기화 및 콜백 설정
        locationProvider = LocationProvider(requireContext()) { latitude, longitude ->
            currentLocation = LatLng.from(latitude, longitude)
            kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(currentLocation))
        }

        initMap()
    }

    @SuppressLint("MissingPermission")
    fun initMap() {
        var loc = LatLng.from(0.0, 0.0)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            loc = LatLng.from(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
        }


        val map = binding.findLocationMapView
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
                this@LocationSelectFragment.kakaoMap = kakaoMap


                // 카메라 움직임이 시작할 때 리스너입니다.
                kakaoMap.setOnCameraMoveStartListener { kakaoMap, gestureType ->
                    binding.addressDisplay.text = "발견 위치로 이동시켜 주세요."
                }

                // 카메라 움직임이 끝날 때 리스너입니다.
                kakaoMap.setOnCameraMoveEndListener { kakaoMap, cameraPosition, gestureType ->
                    Log.d("testt", "카메라 이동 종료")
                    // cameraPosition -> 맵 가운데 위치
                    // 항상 맵 가운데에 핀이 위치해 있으므로(xml에 있습니다) 핀의 위치를 반환.
                    lifecycleScope.launch {
                        val address = withContext(Dispatchers.IO) {
                            // 나중에 viewmodel에서 호출하면 될듯합니다.
                            // LatLng 클래스를 AddressDTO로 반환합니다.
                            // Kakao Local API 사용하였습니다.
                            // DTO 관련은 KakaoLocalResponseDTO 확인하면 됩니다.
                            kakaoLocalRepository.latLngToAddress(cameraPosition.position)
                        }
                        Log.d("testt", address.toString())
                        // 필요한 정보를 DTO에서 뽑아서 사용하시면 됩니다.
                        binding.addressDisplay.text = address.address?.addressName

                        val result = Bundle().apply {
                            putDouble("latitude", cameraPosition.position.latitude)
                            putDouble("longitude", cameraPosition.position.longitude)
                            putString("address", address.address?.addressName)
                        }
                        Log.d(
                            "BundleCheck",
                            "Latitude: ${cameraPosition.position.latitude}, Longitude: ${cameraPosition.position.longitude}, Address: ${address.address?.addressName}"
                        )
                        parentFragmentManager.setFragmentResult("locationRequestKey", result)
                    }
                }
            }
        })

    }
}