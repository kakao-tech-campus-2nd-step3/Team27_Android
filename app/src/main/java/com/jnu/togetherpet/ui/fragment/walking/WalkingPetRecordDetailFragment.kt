package com.jnu.togetherpet.ui.fragment.walking

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jnu.togetherpet.R
import com.jnu.togetherpet.databinding.FragmentWalkingPetResultBinding
import com.jnu.togetherpet.extensions.drawLine
import com.jnu.togetherpet.extensions.formattingLocalDateTimeToString
import com.jnu.togetherpet.ui.viewmodel.walking.WalkingPetRecordViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale


class WalkingPetRecordDetailFragment : Fragment() {

    private var _binding : FragmentWalkingPetResultBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel : WalkingPetRecordViewModel by activityViewModels()
    private var baseTime = 0L
    private var startLoc = LatLng.from(35.180837, 126.904849)


    private lateinit var kakaoMap: KakaoMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalkingPetResultBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap()
    }

    fun initMap(){
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
                kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(startLoc))
                this@WalkingPetRecordDetailFragment.kakaoMap = kakaoMap

                initListener()
            }
        })
    }

    fun displayStartPoint(arrayList: ArrayList<LatLng>) {
        createLabel(arrayList.first())
    }

    fun displayEndPoint(arrayList: ArrayList<LatLng>){
        createLabel(arrayList.last())
    }

    private fun createLabel(pos : LatLng){
        val labelManager = kakaoMap.labelManager
        val style = labelManager
            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.seraching_own_location).setAnchorPoint(0.5f, 1f))) // Todo : 변경 필요
        var label = kakaoMap.getLabelManager()?.getLayer()?.addLabel(LabelOptions.from("center",pos).setStyles(style).setRank(1))
    }


    fun initListener(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.arrayLoc.collectLatest{ arrayLoc ->
                    Log.d("testt", "listener, Array : ${arrayLoc}")
                    if(arrayLoc.isNotEmpty()){
                        displayStartPoint(arrayLoc)
                        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(arrayLoc.first()))
                        kakaoMap.drawLine(arrayLoc)
                        displayEndPoint(arrayLoc)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.distance.collectLatest {
                    binding.distanceResultText.text = "총 ${it}m 산책했어요"
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.base.collectLatest{
                    baseTime = it
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.time.collectLatest {
                    val format = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)
                    format.timeZone = TimeZone.getTimeZone("UTC")
                    binding.timeResultText.text = format.format(it)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.calories.collectLatest {
                    binding.caloriesResultText.text = "${sharedViewModel.petName.value}가 총 ${it}kcal 만큼 소모했어요!"
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.startTime.collectLatest {
                    binding.timeResultRedText.text = "${it.formattingLocalDateTimeToString()} ~ ${sharedViewModel.endTime.value.formattingLocalDateTimeToString()}"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}