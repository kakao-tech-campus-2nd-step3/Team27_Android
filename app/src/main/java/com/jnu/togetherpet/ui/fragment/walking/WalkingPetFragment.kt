package com.jnu.togetherpet.ui.fragment.walking

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jnu.togetherpet.R
import com.jnu.togetherpet.databinding.CustomDialogBinding
import com.jnu.togetherpet.databinding.FragmentWalkingPetBinding
import com.jnu.togetherpet.extensions.drawLine
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.jnu.togetherpet.extensions.removeLine
import com.jnu.togetherpet.ui.viewmodel.walking.WalkingPetViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class WalkingPetFragment : Fragment() {
    private var _binding: FragmentWalkingPetBinding? = null
    private val binding get() = _binding!!
    private val viewModel : WalkingPetViewModel by activityViewModels()
    private var lastLocationIndex : Int = 0 // 마지막으로 지도에 표시했던 위치의 인덱스
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
        viewModel.setPetImage()
    }


    fun initVar(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        initResultLauncher()
        initMap()
        initListener()
    }

    @SuppressLint("MissingPermission")
    fun initResultLauncher(){
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
                        location ->
                        val latLng = LatLng.from(location.latitude, location.longitude)
                        kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(latLng))
                    }
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {

                } else -> {
                Toast.makeText(requireContext(), "권한 거절시 산책 기능이 제한될 수 있습니다.", Toast.LENGTH_SHORT).show()
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
            showStopDialog()
        }

        binding.timeValue.onChronometerTickListener = Chronometer.OnChronometerTickListener {
            viewModel.timerStart()
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.arrayLoc.collect { arrayList ->
                    val array = arrayList.slice(lastLocationIndex..arrayList.lastIndex)
                    Log.d("testt", "lastLocationIndex : $lastLocationIndex, lastIndex : ${arrayList.lastIndex}")
                    kakaoMap?.drawLine(ArrayList(array))
                    createLabel(viewModel.lastLoc.value)
                    viewModel.calculateBetweenTwoLocation(lastLocationIndex, arrayList.lastIndex)
                    viewModel.calculateCalories()
                    if(arrayList.size != 0) lastLocationIndex = arrayList.lastIndex
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.distance.collectLatest {
                    binding.distanceValue.text = it.toString()
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.time.collectLatest {
                    Log.d("testt", "time : ${it}")
                    val format = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)
                    format.timeZone = TimeZone.getTimeZone("UTC")
                    binding.timeValue.text = format.format(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.calories.collectLatest {
                    binding.calorieValue.text = it.toString()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isWalking.collect{
                    if(it){
                        showBoard()
                        binding.walkingStartButton.visibility = View.GONE
                        binding.timeValue.start()
                        Log.d("testt", "loc : ${viewModel.arrayLoc.value.last()}")
                        kakaoMap?.drawLine(viewModel.arrayLoc.value)
                    }
                }
            }
        }


        binding.walkingSavePageButton.setOnClickListener{
            navigateToRecordPage()
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
            override fun getZoomLevel(): Int {
                return 18
            }

            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("testt", "MapReady")
                kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(loc))
                this@WalkingPetFragment.kakaoMap = kakaoMap
                createLabel(loc)
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

    fun hideBoard(){
        binding.walkingDisplayBoard.visibility = View.INVISIBLE
        binding.walkingStopButton.visibility = View.INVISIBLE
        binding.walkingStartButton.visibility = View.VISIBLE
        binding.calorieText.visibility = View.INVISIBLE
        binding.calorieValue.visibility = View.INVISIBLE
        binding.timeText.visibility = View.INVISIBLE
        binding.timeValue.visibility = View.INVISIBLE
        binding.distanceText.visibility = View.INVISIBLE
        binding.distanceValue.visibility = View.INVISIBLE
        binding.walkingGoalText.visibility = View.INVISIBLE
    }



    fun initBoard(){
        binding.calorieValue.text = "0"
        binding.timeValue.text = "00:00:00"
        binding.distanceValue.text = "0"
    }

    fun showStopDialog(){
        val dialogBinding = CustomDialogBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
        val alertDialog = dialogBuilder.create()

        dialogBinding.dialogYesButton.setOnClickListener {
            viewModel.stopLocationTracking()
            binding.timeValue.stop()
            if(!viewModel.isTimeUnderMinTime() && !viewModel.isDistanceUnderMinDistance()) navigateToResultPage()
            else {
                displayToastMessageAboutSave()
                hideBoard()
                viewModel.initValue()
                kakaoMap?.removeLine()
                lastLocationIndex = 0
            }
            alertDialog.dismiss()
        }
        dialogBinding.dialogNoButton.setOnClickListener{
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun navigateToResultPage(){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_frameLayout, WalkingPetResultFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun navigateToRecordPage(){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_frameLayout, WalkingPetRecordFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private fun removeAllLabel(){
        kakaoMap?.labelManager?.clearAll()
    }

    private fun createLabel(pos : LatLng){
        Log.d("testt", "loc : $pos")
        removeAllLabel()
        val labelManager = kakaoMap?.labelManager
        val style = labelManager
            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.walking_my_location_pin).setAnchorPoint(0.5f, 0.5f).setApplyDpScale(true)))
        kakaoMap?.getLabelManager()?.getLayer()?.addLabel(LabelOptions.from("center",pos).setStyles(style))
    }

    private fun displayToastMessageAboutSave(){
        if(viewModel.isTimeUnderMinTime())
            Toast.makeText(requireContext(), "1분 이하의 기록은 저장되지 않습니다.", Toast.LENGTH_SHORT).show()
        else if(viewModel.isDistanceUnderMinDistance())
            Toast.makeText(requireContext(), "10m 이하의 기록은 저장되지 않습니다.", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

