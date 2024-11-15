package com.jnu.togetherpet.ui.fragment.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.jnu.togetherpet.R
import com.jnu.togetherpet.ui.viewmodel.report.RegistrationViewModel
import com.jnu.togetherpet.databinding.FragmentInfoRegistrationPetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationPetFragment : Fragment() {
    private var _binding: FragmentInfoRegistrationPetBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInfoRegistrationPetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            nextButton.setOnClickListener { goToNextScreen() }
        }
    }

    private fun checkNameAndAge(): InputState {
        return if (binding.nameInputField.text.isEmpty()) {
            binding.nameInputField.requestFocus()
            InputState.NOT_EXIST_NAME
        } else if (binding.ageInputField.text.isEmpty()) {
            binding.ageInputField.requestFocus()
            InputState.NOT_EXIST_AGE
        }
        else InputState.EXIST_NAME_AND_AGE
    }

    private fun existNameAndAge(): Boolean {
        return when(checkNameAndAge()) {
            InputState.EXIST_NAME_AND_AGE -> true
            else -> false
        }
    }

    private fun goToNextScreen() {
        if(existNameAndAge()) {
            sharedViewModel.setPetName(binding.nameInputField.text.toString())
            sharedViewModel.setPetAge(binding.ageInputField.text.toString().toLong())
            sharedViewModel.setPetSpecies(binding.speciesInputField.text.toString())
            sharedViewModel.setNeutering(binding.buttonNeuteringTrue.isChecked == true)
            findNavController().navigate(R.id.action_registrationPetFragment_to_registrationResidenceFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class InputState {
        EXIST_NAME_AND_AGE,
        NOT_EXIST_NAME,
        NOT_EXIST_AGE
    }
}