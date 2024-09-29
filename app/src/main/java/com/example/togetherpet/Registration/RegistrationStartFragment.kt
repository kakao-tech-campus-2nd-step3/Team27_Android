package com.example.togetherpet.Registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.togetherpet.R
import com.example.togetherpet.databinding.FragmentInfoRegistrationStartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationStartFragment : Fragment() {
    private var binding : FragmentInfoRegistrationStartBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoRegistrationStartBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            startWhiteButton.setOnClickListener { goToNextScreen() }
            startPinkButton.setOnClickListener { goToFindScreen() }
        }
    }

    private fun goToNextScreen(){
        findNavController().navigate(R.id.action_registrationStartFragment_to_registrationPetFragment)
    }

    private fun goToFindScreen(){
        Toast.makeText(activity, "find", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}