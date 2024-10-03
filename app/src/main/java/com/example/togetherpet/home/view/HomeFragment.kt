package com.example.togetherpet.home.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.togetherpet.databinding.FragmentHomeBinding
import com.example.togetherpet.home.viewModel.HomeViewModel
import com.example.togetherpet.adapter.PetListAdapter
import com.example.togetherpet.testData.viewModel.MissingViewModel
import com.example.togetherpet.testData.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
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

        userViewModel.addDummyUser()
        missingViewModel.addDummyMissingPet()
        homeViewModel.loadData()

        homeViewModel.missingPets.observe(viewLifecycleOwner) { pets ->
            Log.d(
                "yeong",
                "Current missing pets: ${pets.size} (isEmpty: ${pets.isEmpty()})"
            )
            binding.homeMissingPetList.adapter = PetListAdapter(pets)

            if (pets.isEmpty()) {
                binding.homeSos.visibility = View.GONE
                binding.homeLogo.visibility = View.VISIBLE
            } else {
                binding.homeLogo.visibility = View.GONE
                binding.homeSos.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}