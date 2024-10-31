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

    @Inject
    lateinit var kakaoLocalRepository: KakaoLocalRepository

    private var _binding: FragmentLocationSelectBinding? = null
    private val binding get() = _binding!!
    var kakaoMap: KakaoMap? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationLabel: Label
    var trackingManager: TrackingManager? = null


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
                trackingManager = kakaoMap.trackingManager

                kakaoMap.setOnCameraMoveStartListener { kakaoMap, gestureType ->
                    // 카메라 움직임 시작 시 이벤트 호출
                    // 사용자 제스쳐가 아닌 코드에 의해 카메라가 움직이면 GestureType 은 Unknown
                    kakaoMap.getCameraPosition()?.position?.let {
                        Log.d("testt", "camera : ${it}")
                        binding.addressDisplay.text = "발견 위치로 이동시켜 주세요."
                    }
                }

                kakaoMap.setOnCameraMoveEndListener { kakaoMap, cameraPosition, gestureType ->
                    // 카메라 움직임 종료 시 이벤트 호출
                    // 사용자 제스쳐가 아닌 코드에 의해 카메라가 움직이면 GestureType 은 Unknown
                    Log.d("testt", "카메라 이동 종료")
                    lifecycleScope.launch {
                        val address = withContext(Dispatchers.IO) {
                            kakaoLocalRepository.latLngToAddress(cameraPosition.position)
                        }
                        Log.d("testt", address.toString())
                        binding.addressDisplay.text = address.address?.addressName
                    }

                }
            }
        })

    }

    private fun removeAllLabel() {
        kakaoMap?.labelManager?.clearAll()
    }

    private fun createLabel(pos: LatLng) {
        removeAllLabel()
        val labelManager = kakaoMap?.labelManager
        val style = labelManager
            ?.addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(R.drawable.sos_icon).setAnchorPoint(0.5f, 0.5f)
                        .setApplyDpScale(true)
                )
            )
        kakaoMap?.getLabelManager()?.getLayer()
            ?.addLabel(LabelOptions.from("center", pos).setStyles(style)).also {
                if (it != null) {
                    locationLabel = it
                }
            }
    }
}