package com.example.togetherpet.Registration

import android.app.Activity
import android.content.Intent
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
    private var _binding : FragmentInfoRegistrationImageBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel : RegistrationViewModel by activityViewModels()
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInfoRegistrationImageBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            nextButton.setOnClickListener { goToNextScreen() }
            imageInputButton.setOnClickListener { setImage() }
            resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
                if(result.resultCode == Activity.RESULT_OK ){
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

    private fun goToNextScreen(){
        Toast.makeText(activity, "next", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_registrationImageFragment_to_registrationNicknameFragment)
    }

    private fun setImage(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}