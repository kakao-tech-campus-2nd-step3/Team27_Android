package com.jnu.togetherpet.ui.fragment.searching

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.jnu.togetherpet.data.repository.DataStoreRepository
import com.jnu.togetherpet.R
import com.jnu.togetherpet.ui.adapter.SearchingBtnListAdapter
import com.jnu.togetherpet.data.entity.MissingEntity
import com.jnu.togetherpet.data.entity.ReportEntity
import com.jnu.togetherpet.data.repository.KakaoLocalRepository
import com.jnu.togetherpet.databinding.FragmentSearchingPetBinding
import com.jnu.togetherpet.extensions.toBitmap
import com.jnu.togetherpet.ui.adapter.MissingAdapter
import com.jnu.togetherpet.ui.adapter.ReportAdapter
import com.jnu.togetherpet.ui.viewmodel.report.ReportDataViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jnu.togetherpet.extensions.ItemSpacing
import com.jnu.togetherpet.ui.fragment.searching.enums.ButtonType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchingPetFragment : Fragment() {
    //내 반려 동물 missing 여부 저장
    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    //위도, 경도 -> 주소
    @Inject
    lateinit var kakaoLocalRepository: KakaoLocalRepository

    //실종 제보 데이터 저장
    private val reportDataViewModel: ReportDataViewModel by viewModels()

    private var _binding: FragmentSearchingPetBinding? = null
    private val binding get() = _binding!!

    //버튼 목록 : 실종 정보, 제보 정보, (나의 펫)
    private lateinit var searchingBtnListAdapter: SearchingBtnListAdapter

    //Google Play 위치 API
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    private var kakaoMap: KakaoMap? = null

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var centerPos: LatLng = LatLng.from(0.0, 0.0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchingPetBinding.inflate(inflater, container, false)

        //---현재 위치 얻기 위함---
        //FusedLocationProviderClient 등록
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        //권한 요청 초기화
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
                Log.d("testtt", "onMapReady")
                kakaoMap?.setOnCameraMoveEndListener { _, cameraPosition, _ ->
                    centerPos = cameraPosition.position
                }

                observeMissingReports()

                //클릭 이벤트 활성화
                kakaoMap!!.isPoiClickable = true
                checkLocationPermission()
            }


            override fun getZoomLevel(): Int {
                return 18
            }
        })
        setBtnListAdapter()

        //btnList 사이의 간격 설정
        binding.researchingBtnList.addItemDecoration(ItemSpacing(20))

        return binding.root
    }

    private fun setBtnListAdapter() {
        viewLifecycleOwner.lifecycleScope.launch {
            dataStoreRepository.missingStatus.collectLatest { isMissing ->
                dataStoreRepository.petName.collectLatest { petName ->
                    ButtonType.MyPET.setPetName(petName)
                    updateAdapter(isMissing, petName)
                }
            }
        }
    }

    private fun updateAdapter(isMissing: Boolean, petName: String) {
        searchingBtnListAdapter = SearchingBtnListAdapter(isMissing, petName) { clickedItem ->
            handleBtnClick(clickedItem)
        }
        binding.researchingBtnList.adapter = searchingBtnListAdapter
    }

    private fun handleBtnClick(item: ButtonType) {
        reportDataViewModel.updateSelectedBtn(item)
        when (item) {
            ButtonType.MISSING -> {
                binding.searchingMissingList.visibility = View.VISIBLE
                binding.myPetMissingRegisterButton.visibility = View.VISIBLE
                binding.searchingReportBtn.visibility = View.GONE
                clearRecyclerView()
                observeMissingReports()
            }

            ButtonType.REPORT -> {
                binding.searchingMissingList.visibility = View.VISIBLE
                binding.myPetMissingRegisterButton.visibility = View.GONE
                binding.searchingReportBtn.visibility = View.VISIBLE
                clearRecyclerView()
                kakaoMap?.labelManager?.clearAll()
                observeSuspectedReports()
            }

            ButtonType.MyPET -> {
                binding.searchingMissingList.visibility = View.GONE
                binding.myPetMissingRegisterButton.visibility = View.GONE
                binding.searchingReportBtn.visibility = View.GONE
                clearRecyclerView()
                kakaoMap?.labelManager?.clearAll()
                observeMyPetReports()
            }
        }
    }

    private fun observeMissingReports() {
        viewLifecycleOwner.lifecycleScope.launch {
            reportDataViewModel.missingReports.collectLatest { missings ->
                if (missings.isNotEmpty()) {
                    binding.searchingMissingList.adapter =
                        MissingAdapter(missings, kakaoLocalRepository) { missingEntity ->
                            moveMapToLocation(missingEntity.latitude, missingEntity.longitude)
                        }
                    setMissingMarker(missings)
                } else {
                    MissingAdapter(emptyList(), kakaoLocalRepository) { }
                    setMissingMarker(emptyList())
                    Log.d("SearchingPetFragment", "No Missing Data")
                }
            }
        }
    }

    private suspend fun setMissingMarker(missings: List<MissingEntity>) {
        val labelManager: LabelManager? = kakaoMap?.labelManager
        labelManager?.clearAll() // 기존 마커 초기화

        for (pet in missings) {
            delay(500)

            val markerView = LayoutInflater.from(requireContext())
                .inflate(R.layout.pet_img_map_marker, null, true)

            val petImgMarker =
                markerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.petImg_marker)

            Glide.with(this)
                .load(Uri.parse(pet.petImageUrl.first()))
                .apply(RequestOptions())
                .placeholder(R.drawable.main_logo) // 기본 이미지 설정
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        //Log.d("testt", "--- onResourceReady (CustomTarget): $resource")
                        petImgMarker.setImageDrawable(resource)

                        val bitmapImg = markerView.toBitmap()

                        // 마커 스타일 설정
                        val markerStyle = labelManager?.addLabelStyles(
                            LabelStyles.from(LabelStyle.from(bitmapImg))
                        )

                        // 마커 위치 지정
                        val pos = LatLng.from(pet.latitude, pet.longitude)

                        // 레이어 가져오기
                        val layer = labelManager?.layer

                        // 레이어에 라벨 추가
                        layer?.addLabel(
                            LabelOptions.from(pos)
                                .setStyles(markerStyle)
                                .setTag(pet.id)
                        )
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        //Log.d("testt", "--- onLoadCleared (CustomTarget): $placeholder")
                        petImgMarker.setImageDrawable(placeholder)
                    }
                })
        }

        // 마커 클릭 리스너 설정
        kakaoMap?.setOnLabelClickListener { _, _, label ->
            label?.let {
                val missingId = it.tag.toString().toLongOrNull()
                if (missingId != null) {
                    //Toast.makeText(requireContext(), "마커 클릭", Toast.LENGTH_SHORT).show()
                    reportDataViewModel.fetchMissingDetails(missingId)
                    showMissingBottomSheet(missingId)
                }
            }
            true
        }
    }

    private fun showMissingBottomSheet(missingId: Long) {
        val bottomSheetFragment = MissingBottomSheetFragment.newInstance(missingId)
        Log.d("yeong", "miss Id: $missingId")
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }

    private fun showSuspectedBottomSheet(reportId: Long) {
        val bottomSheetFragment = SuspectedBottomSheetFragment.newInstance(reportId)
        Log.d("yeong", "miss Id: $reportId")
        bottomSheetFragment.show(parentFragmentManager, bottomSheetFragment.tag)
    }

    private suspend fun setReportMarker(suspected: List<ReportEntity>) {
        val labelManager: LabelManager? = kakaoMap?.labelManager
        labelManager?.clearAll() // 기존 마커 초기화

        for (pet in suspected) {
            delay(500)

            val markerView = LayoutInflater.from(requireContext())
                .inflate(R.layout.pet_img_map_marker, null, true)

            val petImgMarker =
                markerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.petImg_marker)

            Glide.with(this)
                .load(Uri.parse(pet.imageUrl.first()))
                .apply(RequestOptions())
                .placeholder(R.drawable.main_logo) // 기본 이미지 설정
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        petImgMarker.setImageDrawable(resource)
                        val bitmapImg = markerView.toBitmap()

                        // 마커 스타일 설정
                        val markerStyle = labelManager?.addLabelStyles(
                            LabelStyles.from(LabelStyle.from(bitmapImg))
                        )

                        // 마커 위치 지정
                        val pos = LatLng.from(pet.latitude, pet.longitude)

                        // 레이어 가져오기
                        val layer = labelManager?.layer

                        // 레이어에 라벨 추가
                        layer?.addLabel(

                            LabelOptions.from(pos).setStyles(markerStyle).setTag(pet.id)
                        )
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        petImgMarker.setImageDrawable(placeholder)
                    }
                })
        }

        // 마커 클릭 리스너 설정
        kakaoMap?.setOnLabelClickListener { _, _, label ->
            label?.let {
                val reportId = it.tag.toString().toLongOrNull()
                if (reportId != null) {
                    //Toast.makeText(requireContext(), "마커 클릭", Toast.LENGTH_SHORT).show()
                    reportDataViewModel.fetchSuspectedDetails(reportId)
                    showSuspectedBottomSheet(reportId)
                }
            }
            true
        }
    }

    private fun observeSuspectedReports() {
        viewLifecycleOwner.lifecycleScope.launch {
            reportDataViewModel.nearbySuspectedReports.collectLatest { suspected ->
                if (suspected.isNotEmpty()) {
                    binding.searchingMissingList.adapter =
                        ReportAdapter(suspected, kakaoLocalRepository) { suspectedEntity ->
                            moveMapToLocation(suspectedEntity.latitude, suspectedEntity.longitude)
                        }
                    setReportMarker(suspected)
                } else {
                    binding.searchingMissingList.adapter =
                        ReportAdapter(emptyList(), kakaoLocalRepository) {}
                    setReportMarker(emptyList())
                    Log.d("SearchingPetFragment", "No Suspected Missing Data")
                }
            }
        }
    }

    private fun observeMyPetReports() {
        viewLifecycleOwner.lifecycleScope.launch {
            reportDataViewModel.myPetReports.collectLatest { reports ->
                if (reports.isNotEmpty()) {
                    binding.searchingMissingList.adapter =
                        ReportAdapter(reports, kakaoLocalRepository) { suspectedEntity ->
                            moveMapToLocation(suspectedEntity.latitude, suspectedEntity.longitude)
                        }
                    setReportMarker(reports)
                } else {
                    ReportAdapter(emptyList(), kakaoLocalRepository) { }
                    setReportMarker(emptyList())
                    Log.d("SearchingPetFragment", "No My Pet Observing Data")
                }
            }
        }
    }

    private fun moveMapToLocation(latitude: Double, longitude: Double) {
        val position = LatLng.from(latitude, longitude)
        kakaoMap?.moveCamera(CameraUpdateFactory.newCenterPosition(position))
    }

    private fun clearRecyclerView() {
        binding.searchingMissingList.adapter = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                reportDataViewModel.selectedButton.collectLatest { buttonType ->
                    handleBtnClick(buttonType)
                }
            }
        }

        //목격 제보 버튼 클릭
        binding.searchingReportBtn.setOnClickListener {
            val reportSuspectedMissingPet = ReportSuspectedMissingPetFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_searching, reportSuspectedMissingPet)
                .addToBackStack(null)
                .commit()
        }

        //"내 반려동물 실종등록" 버튼 클릭
        binding.myPetMissingRegisterButton.setOnClickListener {
            val reportMyPet = MyPetReportFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_searching, reportMyPet)
                .addToBackStack(null)
                .commit()
        }

        //refresh 버튼 클릭
        binding.refreshDataBtn.setOnClickListener {
            fetchData()
        }
    }

    private fun fetchData() {
        viewLifecycleOwner.lifecycleScope.launch {
            reportDataViewModel.setCenterPos(centerPos.latitude, centerPos.longitude)
            when (reportDataViewModel.selectedButton.value) {
                ButtonType.MISSING -> {
                    Log.d("parent", "Missing Data Fetch")
                    reportDataViewModel.fetchMissingReports(centerPos.latitude, centerPos.longitude)
                }

                ButtonType.REPORT -> {
                    Log.d("parent", "Reported Data Fetch")
                    reportDataViewModel.fetchSuspectedReports(
                        centerPos.latitude,
                        centerPos.longitude
                    )
                }

                ButtonType.MyPET -> {
                    Log.d("parent", "MyPet Data Fetch")
                    reportDataViewModel.fetchMyPetReports()
                }
            }
        }
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
            //setMarker()
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
                displayLocation(latitude, longitude)
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