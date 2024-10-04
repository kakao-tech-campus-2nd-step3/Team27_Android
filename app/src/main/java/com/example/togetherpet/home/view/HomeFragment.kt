package com.example.togetherpet.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.togetherpet.databinding.FragmentHomeBinding
import com.example.togetherpet.home.viewModel.HomeViewModel
import com.example.togetherpet.adapter.PetListAdapter
import com.example.togetherpet.testData.viewModel.MissingViewModel
import com.example.togetherpet.testData.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

            homeViewModel.isDataLoaded.collect { isLoaded ->
                if (isLoaded) {
                    // 데이터 로드가 완료된 후에만 관찰을 시작
                    homeViewModel.missingPets.collect { missingInfo ->
                        Log.d("yeong", "Missing data: $missingInfo")

                        if (missingInfo.isNotEmpty()) {
                            binding.homeMissingPetList.adapter = PetListAdapter(missingInfo)
                            binding.homeLogo.visibility = View.GONE
                            binding.homeSos.visibility = View.VISIBLE
                        } else {
                            binding.homeSos.visibility = View.GONE
                            binding.homeLogo.visibility = View.VISIBLE
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
}