package com.example.togetherpet.home.view

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.togetherpet.R
import com.example.togetherpet.databinding.FragmentHomeBinding
import com.example.togetherpet.home.viewModel.HomeViewModel
import com.example.togetherpet.adapter.PetListAdapter
import com.example.togetherpet.testData.viewModel.MissingViewModel
import com.example.togetherpet.testData.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()
    private val missingViewModel: MissingViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.homeMissingPetList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewLifecycleOwner.lifecycleScope.launch {
            val userJob = async { userViewModel.addDummyUser() }
            val missingJob = async { missingViewModel.addDummyMissingPet() }

            //더미 데이터 생성이 완료될 때까지 기다림
            awaitAll(userJob, missingJob)

            homeViewModel.loadData()

            homeViewModel.isDataLoaded.collectLatest { isLoaded ->
                if (isLoaded) {
                    // 데이터 로드가 완료된 후에만 관찰을 시작
                    homeViewModel.missingPets.collectLatest { missingInfo ->
                        Log.d("yeong", "Missing data: $missingInfo")

                        if (missingInfo.isNotEmpty()) {
                            binding.homeMissingPetList.adapter =
                                PetListAdapter(requireContext(), missingInfo)
                            binding.homeLogo.visibility = View.GONE
                            binding.homeSos.visibility = View.VISIBLE
                        } else {
                            binding.homeSos.visibility = View.GONE
                            binding.homeLogo.visibility = View.VISIBLE
                        }
                        homeViewModel.user.collectLatest { user ->
                            Log.d("yeong", "User data: $user")
                            val userNickname = user?.userNickname
                            val petName = user?.petName
                            val petImgUrl = user?.petImgUrl

                            val avgCount = user?.avgWalkCount.toString()
                            val avgDistance = user?.avgWalkDistance.toString()
                            val avgTime = user?.avgWalkTime

                            val testText = "안녕하세요,  <b>${petName}</b> 보호자 <b>${userNickname}</b> 님"
                            binding.homeGreeting.text =
                                Html.fromHtml(testText, Html.FROM_HTML_MODE_LEGACY)

                            binding.homeWalkingTitle.text =
                                getString(R.string.home_walking_title, petName)

                            Glide.with(requireContext())
                                .load(petImgUrl)
                                .apply(
                                    RequestOptions().centerCrop()
                                        .transform(RoundedCorners(dpToPx(requireContext(), 10)))
                                )
                                .into(binding.homeProfileImg)

                            binding.homeTotalCount.text = user?.todayWalkCount.toString()
                            binding.homeTotalDistance.text = user?.todayWalkDistance.toString()
                            binding.homeTotalTime.text = user?.todayWalkTime

                            binding.homeAvgCount.text = getString(R.string.home_avg_count, avgCount)
                            binding.homeAvgDistance.text =
                                getString(R.string.home_avg_distance, avgDistance)
                            binding.homeAvgTime.text = getString(R.string.home_avg_time, avgTime)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}