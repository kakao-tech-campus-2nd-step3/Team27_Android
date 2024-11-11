package com.example.togetherpet.Registration

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.togetherpet.R
import com.example.togetherpet.dashboard.view.DashboardActivity
import com.example.togetherpet.databinding.FragmentInfoRegistrationImageBinding
import com.example.togetherpet.databinding.FragmentWalkingPetResultBinding
import dagger.hilt.android.AndroidEntryPoint
import java.net.URI


@AndroidEntryPoint
class RegistrationImageFragment : Fragment() {
    private var _binding: FragmentInfoRegistrationImageBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: RegistrationViewModel by activityViewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoRegistrationImageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            nextButton.setOnClickListener { goToNextScreen() }
            imageInputButton.setOnClickListener {
                checkPermission()
                setImage()
            }
            resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val uri = result.data?.data
                        if (uri != null) {
                            Glide.with(requireContext())
                                .load(uri)
                                .apply(
                                    RequestOptions().centerCrop()
                                )
                                .into(binding.animalImage)
                            sharedViewModel.setPetImage(uri)
                        }
                    }
                }
        }
    }

    private fun goToNextScreen() {
        if (existimage()) {
            findNavController().navigate(R.id.action_registrationImageFragment_to_registrationNicknameFragment)
        }
    }

    private fun setImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkImage(): InputState {
        return if (sharedViewModel.petImage.value == Uri.EMPTY) InputState.NOT_EXIST_IMAGE
        else InputState.EXIST_IMAGE
    }

    private fun existimage() : Boolean{
        return when(checkImage()){
            InputState.EXIST_IMAGE -> true
            InputState.NOT_EXIST_IMAGE -> false
        }
    }

    private fun checkPermission(){
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    101
                )
            }
        }
    }

    enum class InputState {
        EXIST_IMAGE,
        NOT_EXIST_IMAGE
    }

}