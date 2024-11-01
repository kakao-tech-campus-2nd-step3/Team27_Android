package com.example.togetherpet.searching.report.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.togetherpet.databinding.ReportSuspectedMissingPetFragmentBinding
import com.example.togetherpet.searching.report.viewModel.ReportSuspectedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportSuspectedMissingPetFragment : Fragment() {
    private var _binding: ReportSuspectedMissingPetFragmentBinding? = null
    private val binding get() = _binding!!

    private val reportSuspectedViewModel : ReportSuspectedViewModel by viewModels()

    private lateinit var resultLauncher : ActivityResultLauncher<Intent>

    private var imgUri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ReportSuspectedMissingPetFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // resultLauncher 초기화
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    Glide.with(this)
                        .load(uri)
                        .into(binding.reportMissingImg)
                    imgUri = uri
                }
            }
        }

        //이미지 업로드
        binding.imgUploadBtn.setOnClickListener {
            setImage()
        }

        //'제보 하기' 클릭
        binding.reportMissingReportBtn.setOnClickListener {
            sendReport()
        }

    }

    private fun setImage(){
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        resultLauncher.launch(intent)
    }

    private fun sendReport() {
        setData()
    }

    private fun setData(){
        //로직 분리 필요
        val color = binding.reportMissingColor.text.toString()
        val gender = binding.reportMissingGender.text.toString()
        val species = binding.reportMissingSpecies.text.toString()
        // 날짜, 위치 선택 팝업이 필요함
        //val date = binding.reportMissingTime.text
        //val location = binding.reportMissingLocation.text
        val info = binding.reportMissingInfoDetail.text.toString()

        /*reportSuspectedViewModel.reportSuspected(
            color = color, gender = gender, breed = species, description = info, foundLongitude = 0.0, foundLatitude = 0.0, foundDate =2024-11-01 14:30:45.123
        )*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}