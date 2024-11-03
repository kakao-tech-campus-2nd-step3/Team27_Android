package com.example.togetherpet.fragment

import android.R.attr.label
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.togetherpet.R
import com.example.togetherpet.data.repository.KakaoLocalRepository
import com.example.togetherpet.databinding.FragmentLocationSelectBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.TrackingManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class LocationSelectFragment : Fragment() {

    // 일단 홈화면에서 이미지 클릭하면 넘어오도록 설정했습니다.... 추가로 변경해주시면 될 것 같습니다.
    @Inject
    // 나중에 viewmodel과 연결해서 사용하시면 될거같습니다.
    lateinit var kakaoLocalRepository: KakaoLocalRepository

    private var _binding: FragmentLocationSelectBinding? = null
    private val binding get() = _binding!!
    var kakaoMap: KakaoMap? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
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
                    }

                }
            }
        })

    }
}