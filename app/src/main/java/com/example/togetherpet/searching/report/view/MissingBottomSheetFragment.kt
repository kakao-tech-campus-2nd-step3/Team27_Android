package com.example.togetherpet.searching.report.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.togetherpet.R
import com.example.togetherpet.utils.DpUtils
import com.example.togetherpet.databinding.MissingBottomSheetBinding
import com.example.togetherpet.searching.report.viewModel.ReportDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MissingBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: MissingBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val reportDataViewModel: ReportDataViewModel by activityViewModels ()

    companion object {
        private const val ARG_MISSING_ID = "missing_id"

        // newInstance 메서드를 통해 missingId 값을 전달하도록 설정
        fun newInstance(missingId: Long): MissingBottomSheetFragment {
            val fragment = MissingBottomSheetFragment()
            val args = Bundle()
            args.putLong(ARG_MISSING_ID, missingId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MissingBottomSheetBinding.inflate(inflater, container, false)
        val missingId = arguments?.getLong(ARG_MISSING_ID)
            ?: throw IllegalArgumentException("Missing ID 없음")
        observeMissingDetails()
        return binding.root
    }

    private fun observeMissingDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            reportDataViewModel.missingDetail.collectLatest { detail ->
                Log.d("MissingBottomSheetFragment", "Received detail: $detail")
                detail?.let {
                    updateBottomSheet(
                        petName = it.name ?: "Unknown",
                        species = it.breed ?: "Unknown",
                        age = it.birthMonth?.toString() ?: "Unknown",
                        missingPlace = "${it.latitude}, ${it.longitude}",
                        addInfo = it.description ?: "No additional info",
                        url = it.petImageUrl.firstOrNull()
                    )
                }
            }
        }
    }

    private fun updateBottomSheet(
        petName: String,
        species: String,
        age: String,
        missingPlace: String,
        addInfo: String,
        url: String?
    ) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // '제보 하기' 클릭 -> ReportMissingPetFragment 전환
        binding.myPetMissingRegisterButton.setOnClickListener {
            val missingId = arguments?.getLong(ARG_MISSING_ID)
                ?: throw IllegalArgumentException("MissingId 없음")
            val reportFragment = ReportMissingPetFragment.newInstance(missingId)

            parentFragmentManager.beginTransaction()
                .replace(R.id.home_frameLayout, reportFragment)
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