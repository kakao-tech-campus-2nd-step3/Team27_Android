package com.example.togetherpet.searching

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.togetherpet.R
import com.example.togetherpet.utils.DpUtils
import com.example.togetherpet.databinding.MissingBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MissingBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: MissingBottomSheetBinding? = null
    private val binding get() = _binding!!

    /*// 임시 변수
    private var petName: String? = null
    private var species: String? = null
    private var age: String? = null
    private var missingPlace: String? = null
    private var addInfo: String? = null
    private var url: String? = null*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MissingBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun updateBottomSheet(
        petName: String,
        species: String,
        age: String,
        missingPlace: String,
        addInfo: String,
        url: String
    ) {
        Log.d("yeong", "update까지 옴")
        /*this.petName = petName
        this.species = species
        this.age = age
        this.missingPlace = missingPlace
        this.addInfo = addInfo
        this.url = url*/

        Log.d("yeong", url)

        if (_binding != null && isAdded) {
            binding.missingBottomPetName.text = petName
            binding.missingBottomSpeciesText.text = species
            binding.missingBottomAgeText.text = age
            binding.missingBottomMissingPlaceText.text = missingPlace
            binding.missingBottomAddInfo.text = addInfo

            Glide.with(this)
                .load(url)
                .apply(
                    RequestOptions().centerCrop()
                        .transform(RoundedCorners(DpUtils.dpToPx(requireContext(), 85)))
                )
                .into(binding.missingBottomPetImg)

            Log.d("yeong", "BottomSheet 업데이트 완료")
        }
    }

    /*private fun displayData() {
        _binding?.apply {
            petName?.let { missingBottomPetName.text = it }
            species?.let { missingBottomSpeciesText.text = it }
            age?.let { missingBottomAgeText.text = it }
            missingPlace?.let { missingBottomMissingPlaceText.text = it }
            addInfo?.let { missingBottomAddInfoText.text = it }

            url?.let {
                Glide.with(this@MissingBottomSheetFragment)
                    .load(it)
                    .apply(
                        RequestOptions().centerCrop()
                            .transform(RoundedCorners(DpUtils.dpToPx(requireContext(), 85)))
                    )
                    .into(missingBottomPetImg)
            }

            Log.d("yeong", "뷰에 데이터가 적용됨")
        }
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // '제보 하기' 클릭 -> ReportMissingPetFragment 전환
        binding.myPetMissingRegisterButton.setOnClickListener {
            val reportFragment = ReportMissingPetFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_myPetMissing, reportFragment)
                .addToBackStack(null)
                .commit()

            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding?.let {
            Glide.with(this).clear(it.missingBottomPetImg)
        }
        _binding = null
    }
}