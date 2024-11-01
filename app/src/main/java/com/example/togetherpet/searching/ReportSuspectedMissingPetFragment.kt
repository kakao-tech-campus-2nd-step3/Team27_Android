package com.example.togetherpet.searching

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
import com.bumptech.glide.Glide
import com.example.togetherpet.data.dto.ReportCreateRequestDTO
import com.example.togetherpet.databinding.ReportSuspectedMissingPetFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class ReportSuspectedMissingPetFragment : Fragment() {
    private var _binding: ReportSuspectedMissingPetFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultLauncher : ActivityResultLauncher<Intent>

    //로직 분리 필요
    private var img_uri : Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    img_uri = uri
                }
            }
        }

        binding.imgUploadBtn.setOnClickListener {
            setImage()
        }

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

        val reportRequest = ReportCreateRequestDTO(
            color = color,
            description = info,
            gender = gender,
            breed = species,
            missingId = null,
            foundDate = LocalDateTime.now(),
            foundLatitude = 0.0,
            foundLongitude = 0.0
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}