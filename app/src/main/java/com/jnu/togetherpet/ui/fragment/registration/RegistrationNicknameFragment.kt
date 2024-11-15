package com.jnu.togetherpet.ui.fragment.registration

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.jnu.togetherpet.ui.viewmodel.report.RegistrationViewModel
import com.jnu.togetherpet.ui.activity.dashboard.DashboardActivity
import com.jnu.togetherpet.databinding.FragmentInfoRegistrationNicknameBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class RegistrationNicknameFragment : Fragment() {
    private var _binding: FragmentInfoRegistrationNicknameBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: RegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInfoRegistrationNicknameBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            finishButton.setOnClickListener {
                setUserName()
                goToHomeActivitiy()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.petName.collectLatest { petName ->
                    binding.nicknameMainText.text = "안녕하세요, ${petName} 보호자님\n닉네임을 입력해 주세요"
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.petImage.collectLatest { petImage ->
                    Glide.with(requireContext())
                        .load(petImage)
                        .centerCrop()
                        .into(binding.animalImage)
                }
            }
        }

    }

    private fun setUserName() {
        sharedViewModel.setUserName(_binding?.nicknameInputField?.text.toString())
    }

    private fun goToHomeActivitiy() {
        sharedViewModel.registerUserAndPet(getFileFromUri(sharedViewModel.petImage.value))
        //홈으로 이동
        navigateToHomeActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun absolutelyPath(uri: Uri): String? {
        Log.d("testt", "$uri")
        val contentResolver = requireContext().contentResolver
        var filePath: String? = null

        if (uri.scheme == "content") {
            val projection = arrayOf(android.provider.MediaStore.Images.Media.DATA)
            contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
                val columnIndex = cursor.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex)
                }
                Log.d("testt", "filePath1 : $filePath ")
            }
        } else if (uri.scheme == "file") {
            filePath = uri.path
            Log.d("testt", "filePath2 : $filePath ")
        }

        return filePath
    }

    fun getFileFromUri(uri: Uri): File {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: return File("null")
        val file = File(requireContext().cacheDir, "temp_image.jpg")
        file.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        return file
    }


    private fun navigateToHomeActivity() {
        val intent = Intent(requireActivity(), DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish() // 현재 액티비티를 종료하여 뒤로 가기를 막음
    }

}