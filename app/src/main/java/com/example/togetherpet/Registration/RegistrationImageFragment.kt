package com.example.togetherpet.Registration

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.togetherpet.R
import com.example.togetherpet.dashboard.view.DashboardActivity
import com.example.togetherpet.databinding.FragmentInfoRegistrationImageBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationImageFragment : Fragment() {
    private var binding : FragmentInfoRegistrationImageBinding? = null
    private val sharedViewModel : RegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoRegistrationImageBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            nextButton.setOnClickListener { goToNextScreen() }
        }
    }

    private fun goToNextScreen(){
        Toast.makeText(activity, "next", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_registrationImageFragment_to_registrationNicknameFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}