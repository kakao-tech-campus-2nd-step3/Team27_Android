package com.example.togetherpet.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.togetherpet.R
import com.example.togetherpet.databinding.FragmentWalkingPetBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class WalkingPetFragment : Fragment() {
    private var _binding: FragmentWalkingPetBinding? = null
    private val binding get() = _binding!!
    private val viewModel : WalkingPetViewModel by viewModels()
    var kakaoMap : KakaoMap? = null
    lateinit var locationPermissionRequest : ActivityResultLauncher<Array<String>>
    lateinit var fusedLocationClient : FusedLocationProviderClient


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



    fun drawLine(arrayList: ArrayList<LatLng>){
        val layer = kakaoMap?.routeLineManager?.layer
        val lineStyle = RouteLineStyle.from(16f, Color.RED)
        lineStyle.strokeColor = Color.BLACK
        val stylesSet = RouteLineStylesSet.from(
            RouteLineStyles.from(lineStyle)
        )
        val segment = RouteLineSegment.from(
            arrayList
        ).setStyles(stylesSet.getStyles(0))

        val options = RouteLineOptions.from(segment)
            .setStylesSet(stylesSet)

        val routeLine = layer?.addRouteLine(options)
    }

    fun initVar(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        initResultLauncher()
        initMap()
        initListener()
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
            binding.timeValue.start()
            binding.timeValue.text = "00:00:00"
            viewModel.setTimeBase()
            binding.timeValue.onChronometerTickListener = Chronometer.OnChronometerTickListener {
                viewModel.timerStart()
            }
        }
        binding.walkingDisplayBoard.setOnClickListener{
            // 지도의 스와이프을 막기 위해서 생성. 실제로 하는 역할 X
        }
        binding.walkingStopButton.setOnClickListener{
            viewModel.stopLocationTracking()
            binding.timeValue.stop()
            showStopDialog()
            navigateToResultPage()
        }
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.arrayLastTwoLoc.collect {
                    drawLine(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.distance.collect {
                    binding.distanceValue.text = it.toString()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.time.collect {
                    Log.d("testt", "time : ${it}")
                    val format = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)
                    format.timeZone = TimeZone.getTimeZone("UTC")
                    binding.timeValue.text = format.format(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.calories.collect {
                    binding.calorieValue.text = it.toString()
                }
            }
        }

    }

    fun initMap(){
        var loc = LatLng.from(0.0, 0.0)
        checkPermission()
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                loc = LatLng.from(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
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
        viewModel.startLocationTracking()
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
        }
        if (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            )
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

    fun showStopDialog(){
        // TODO : 다이얼로그 띄워야함
        Toast.makeText(requireContext(), "취소 버튼 클릭", Toast.LENGTH_SHORT).show()
    }

    fun navigateToResultPage(){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_frameLayout, WalkingPetResultFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

