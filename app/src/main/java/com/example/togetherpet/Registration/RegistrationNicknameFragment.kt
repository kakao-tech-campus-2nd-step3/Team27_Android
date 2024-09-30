package com.example.togetherpet.Registration

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.togetherpet.dashboard.view.DashboardActivity
import com.example.togetherpet.databinding.FragmentInfoRegistrationImageBinding
import com.example.togetherpet.databinding.FragmentInfoRegistrationNicknameBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegistrationNicknameFragment : Fragment() {
    private var binding : FragmentInfoRegistrationNicknameBinding? = null
    private val sharedViewModel : RegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoRegistrationNicknameBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            finishButton.setOnClickListener { goToHomeActivitiy() }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                sharedViewModel.petName.collect{ petName ->
                    binding?.nicknameMainText?.text = "안녕하세요, ${petName} 보호자님\n닉네임을 입력해 주세요"
                }
            }
        }

    }

    private fun goToHomeActivitiy(){
        sharedViewModel.sendPetInfoToServer()
        navigateToHomeActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(requireActivity(), DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish() // 현재 액티비티를 종료하여 뒤로 가기를 막음
    }

}