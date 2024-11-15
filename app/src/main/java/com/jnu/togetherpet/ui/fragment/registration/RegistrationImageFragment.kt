package com.jnu.togetherpet.ui.fragment.registration

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.jnu.togetherpet.R
import com.jnu.togetherpet.ui.viewmodel.report.RegistrationViewModel
import com.jnu.togetherpet.databinding.FragmentInfoRegistrationImageBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationImageFragment : Fragment() {
    private var _binding: FragmentInfoRegistrationImageBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: RegistrationViewModel by activityViewModels()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<Array<String>>


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
                checkPermissionAndRequest()
            }
            resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val uri = result.data?.data
                        if (uri != null) {
                            Glide.with(requireContext())
                                .load(uri)
                                .centerCrop()
                                .into(binding.animalImage)
                            sharedViewModel.setPetImage(uri)
                        }
                    }
                }

            permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true ||
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true)) {
                    // 권한이 허용되었을 경우 이미지 설정 메서드 호출
                    setImage()
                } else {
                    Toast.makeText(requireContext(), "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
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


    private fun checkPermissionAndRequest(){
        val permissions = mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        }

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            setImage()
        }
    }

    enum class InputState {
        EXIST_IMAGE,
        NOT_EXIST_IMAGE
    }

}