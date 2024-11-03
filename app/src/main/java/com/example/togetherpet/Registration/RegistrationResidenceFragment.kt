package com.example.togetherpet.Registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.togetherpet.R
import com.example.togetherpet.databinding.FragmentInfoRegistrationResidenceBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationResidenceFragment : Fragment() {
    private var binding : FragmentInfoRegistrationResidenceBinding? = null
    private val sharedViewModel : RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoRegistrationResidenceBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            nextButton.setOnClickListener { goToNextScreen() }
        }
    }

    private fun goToNextScreen(){
        sharedViewModel.setPetFeature(binding?.featureInputField?.text.toString())
        findNavController().navigate(R.id.action_registrationResidenceFragment_to_registrationImageFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}