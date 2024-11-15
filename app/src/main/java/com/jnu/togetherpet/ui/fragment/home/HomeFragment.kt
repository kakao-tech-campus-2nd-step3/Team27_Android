package com.jnu.togetherpet.ui.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.jnu.togetherpet.data.repository.DataStoreRepository
import com.jnu.togetherpet.R
import com.jnu.togetherpet.data.repository.KakaoLocalRepository
import com.jnu.togetherpet.databinding.FragmentHomeBinding
import com.jnu.togetherpet.ui.viewmodel.walking.WalkingPetRecordViewModel
import com.jnu.togetherpet.ui.adapter.MissingAdapter
import com.jnu.togetherpet.ui.viewmodel.report.ReportDataViewModel
import com.jnu.togetherpet.ui.fragment.searching.SearchingPetFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jnu.togetherpet.utils.DpUtils.dpToPx
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    @Inject
    lateinit var kakaoLocalRepository: KakaoLocalRepository

    //산책 데이터
    private val walkingPetRecordViewModel: WalkingPetRecordViewModel by viewModels()

    //실종 제보 데이터
    private val reportDataViewModel: ReportDataViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var locationPermissionRequest: ActivityResultLauncher<String>

    private lateinit var reportAdapter: MissingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

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
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        fetchMissingData()
        setupRecyclerView()
        observeMissingReports()

        return binding.root
    }

    private fun setupRecyclerView() {
        Log.d("HomeFragment", "Initializing RecyclerView")
        reportAdapter = MissingAdapter(emptyList(), kakaoLocalRepository) { missingEntity ->
            val searchingFragment = SearchingPetFragment()

            val bundle = Bundle().apply {
                putString("missingId", missingEntity.petId.toString())
            }
            searchingFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_home, searchingFragment)
                .addToBackStack(null)
                .commit()
        }
        binding.homeMissingPetList.adapter = reportAdapter
    }

    private fun observeMissingReports() {
        viewLifecycleOwner.lifecycleScope.launch {
            reportDataViewModel.missingReports.collectLatest { missings ->
                if (missings.isEmpty()) {
                    Log.d("HomeFragment", "Missing db 데이터 없음")
                    binding.homeLogo.visibility = View.VISIBLE
                    binding.homeSos.visibility = View.GONE
                } else {
                    // 데이터가 있으면 로고 숨기고 RecyclerView 표시
                    Log.d("HomeFragment", "Missing 데이터베이스에 데이터 있음: ${missings.size} 개")
                    binding.homeLogo.visibility = View.GONE
                    binding.homeSos.visibility = View.VISIBLE
                    reportAdapter.updateReports(missings)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    Log.d("HomeFragment", "현재 위치: $latitude / $longitude")
                    fetchMissingData()
                } else {
                    Log.d("HomeFragment", "현재 위치를 가져올 수 없습니다.")
                }
            }
    }

    private fun fetchMissingData() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (latitude != 0.0 && longitude != 0.0) {
                reportDataViewModel.fetchMissingReports(
                    reportDataViewModel.centerPos.value.latitude,
                    reportDataViewModel.centerPos.value.longitude
                )
            } else {
                Log.d("HomeFragment", "위치 정보가 없습니다.")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //---유저 정보 띄우기---
        //case 1: 가입시 등록한 정보 사용
        setupUserInfo()

        //[추가할 부분] case 2: 이미 등록한 유저 -> 데이터 받아서 사용

        //---산책 정보 띄우기---
        viewLifecycleOwner.lifecycleScope.launch {
            walkingPetRecordViewModel.walkingData.collectLatest { data ->
                binding.homeTotalCount.text = "${data.todayWalkCount}"
                binding.homeTotalDistance.text = "${data.distance}"
                binding.homeTotalTime.text = data.time
            }
        }

        walkingPetRecordViewModel.getRecordToLocal(LocalDate.now())

        //[추가할 부분] 평균 데이터 받아서 사용 (api 구현 여부 확인 필요)
        val avgCount = "-"
        val avgDistance = "-"
        val avgTime = "-"
        binding.homeAvgCount.text = getString(R.string.home_avg_count, avgCount)
        binding.homeAvgDistance.text =
            getString(R.string.home_avg_distance, avgDistance)
        binding.homeAvgTime.text = getString(R.string.home_avg_time, avgTime)
    }

    private fun setupUserInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            val petNameFlow = dataStoreRepository.petName
            val userNameFlow = dataStoreRepository.userName
            val imgUriFlow = dataStoreRepository.imgUri

            // petName, userName, imgUri 값을 수집
            petNameFlow.collectLatest { petName ->
                userNameFlow.collectLatest { userName ->
                    imgUriFlow.collectLatest { imgUri ->
                        // UI에 표시할 텍스트 설정
                        val greetingText = "안녕하세요,  <b>$petName</b> 보호자 <b>$userName</b> 님"
                        binding.homeGreeting.text =
                            Html.fromHtml(greetingText, Html.FROM_HTML_MODE_LEGACY)

                        // 이미지 로드
                        Glide.with(this@HomeFragment)
                            .load(Uri.parse(imgUri))
                            .apply(
                                RequestOptions().centerCrop()
                                    .transform(RoundedCorners(dpToPx(requireContext(), 10)))
                            )
                            .into(binding.homeProfileImg)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}