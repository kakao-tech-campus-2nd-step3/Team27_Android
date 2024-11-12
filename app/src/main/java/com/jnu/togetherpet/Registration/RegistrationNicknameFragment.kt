package com.jnu.togetherpet.Registration

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jnu.togetherpet.dashboard.view.DashboardActivity
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
                        .apply(
                            RequestOptions().centerCrop()
                        )
                        .into(binding.animalImage)
                }
            }
        }

    }

    private fun setUserName() {
        sharedViewModel.setUserName(_binding?.nicknameInputField?.text.toString())
    }

    private fun goToHomeActivitiy() {
        sharedViewModel.registerUserAndPet(File(absolutelyPath(sharedViewModel.petImage.value, requireContext())))
        //홈으로 이동
        navigateToHomeActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(requireActivity(), DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish() // 현재 액티비티를 종료하여 뒤로 가기를 막음
    }

}