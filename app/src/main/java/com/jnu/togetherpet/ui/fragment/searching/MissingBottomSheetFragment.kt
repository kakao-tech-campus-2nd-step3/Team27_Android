package com.jnu.togetherpet.ui.fragment.searching

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
import com.jnu.togetherpet.R
import com.jnu.togetherpet.data.repository.KakaoLocalRepository
import com.jnu.togetherpet.utils.DpUtils
import com.jnu.togetherpet.databinding.MissingBottomSheetBinding
import com.jnu.togetherpet.ui.viewmodel.report.ReportDataViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.vectormap.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MissingBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: MissingBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val reportDataViewModel: ReportDataViewModel by activityViewModels ()
    @Inject
    lateinit var kakaoLocalRepository: KakaoLocalRepository

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
        reportDataViewModel.fetchMissingDetails(missingId)
        observeMissingDetails()
        return binding.root
    }

    private fun observeMissingDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            reportDataViewModel.missingDetail.collectLatest { detail ->
                Log.d("yeong", "Received detail: $detail")
                detail?.let {
                    val address = withContext(Dispatchers.IO) {
                        try {
                            kakaoLocalRepository.latLngToAddress(LatLng.from(it.latitude, it.longitude))
                        } catch (e: Exception) {
                            Log.e("MissingBottomSheetFragment", "Failed to get address", e)
                            null
                        }
                    }
                    updateBottomSheet(
                        petName = it.name ?: "Unknown",
                        species = it.breed ?: "Unknown",
                        age = it.birthMonth?.toString() ?: "Unknown",
                        //missingPlace = "${it.latitude}, ${it.longitude}",
                        missingPlace = address?.address?.addressName ?: "Unknown",
                        addInfo = it.description ?: "No additional info",
                        url = it.petImageUrl.firstOrNull()
                    )

                    Log.d("yeong","address 변환 : $address")
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
            binding.missingBottomAgeText.text = String.format("%s개월", age)
            binding.missingBottomMissingPlaceText.text = missingPlace
            binding.missingBottomAddInfoText.text = addInfo

            url?.let {
                Glide.with(this)
                    .load(it)
                    .apply(
                        RequestOptions().centerCrop()
                            .transform(RoundedCorners(DpUtils.dpToPx(requireContext(), 85)))
                    )
                    .into(binding.missingBottomPetImg)
            } ?: Log.d("MissingBottomSheetFragment", "Image URL is null")

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