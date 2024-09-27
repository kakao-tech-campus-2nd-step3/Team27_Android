package com.example.togetherpet.Registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.togetherpet.R
import com.example.togetherpet.databinding.FragmentInfoRegistrationPetBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationPetFragment : Fragment() {
    private var binding : FragmentInfoRegistrationPetBinding? = null
    private val sharedViewModel : RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoRegistrationPetBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            nextButton.setOnClickListener { goToNextScreen() }
        }
    }

    private fun goToNextScreen(){
        sharedViewModel.setPetName(binding?.nameInputField?.text.toString())
        sharedViewModel.setPetAge(binding?.ageInputField?.text.toString().toInt())
        sharedViewModel.setPetSpecies(binding?.speciesInputField?.text.toString())
        sharedViewModel.setNeutering(binding?.buttonNeuteringTrue?.isChecked == true)
        findNavController().navigate(R.id.action_registrationPetFragment_to_registrationResidenceFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}