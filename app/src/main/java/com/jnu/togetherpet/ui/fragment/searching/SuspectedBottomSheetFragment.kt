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
import com.jnu.togetherpet.data.repository.KakaoLocalRepository
import com.jnu.togetherpet.databinding.ReportInfoBottomSheetBinding
import com.jnu.togetherpet.extensions.formatDateTime
import com.jnu.togetherpet.ui.viewmodel.report.ReportDataViewModel
import com.jnu.togetherpet.utils.DpUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.vectormap.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SuspectedBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: ReportInfoBottomSheetBinding? = null
    private val binding get() = _binding!!
    private val reportDataViewModel: ReportDataViewModel by activityViewModels()

    @Inject
    lateinit var kakaoLocalRepository: KakaoLocalRepository

    companion object {
        private const val ARG_REPORT_ID = "report_id"

        fun newInstance(reportId: Long): SuspectedBottomSheetFragment {
            val fragment = SuspectedBottomSheetFragment()
            val args = Bundle()
            args.putLong(ARG_REPORT_ID, reportId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ReportInfoBottomSheetBinding.inflate(inflater, container, false)
        val reportId = arguments?.getLong(ARG_REPORT_ID)
            ?: throw IllegalArgumentException("Report ID 없음")
        reportDataViewModel.clearSuspectedDetail()
        reportDataViewModel.fetchSuspectedDetails(reportId)
        observeReportDetails(reportId)
        return binding.root
    }

    private fun observeReportDetails(reportId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            reportDataViewModel.suspectedDetail.collectLatest { detail ->
                Log.d("MissingBottomSheetFragment", "Received detail: $detail")
                detail?.let {
                    val address = withContext(Dispatchers.IO) {
                        try {
                            kakaoLocalRepository.latLngToAddress(LatLng.from(it.latitude, it.longitude))
                        } catch (e: Exception) {
                            Log.e("MissingBottomSheetFragment", "Failed to get address", e)
                            null
                        }
                    }
                    val date = it.foundDate?.let { it1 -> formatDateTime(it1) }
                    updateBottomSheet(
                        url = it.imageUrl.firstOrNull(),
                        missingPlace = address?.address?.addressName ?: "Unknown",
                        missingDate = date ?: "Unknown",
                        description = it.description ?: "Unknown",
                        reporterName = it.reporterName
                    )
                }
            }
        }
    }

    private fun updateBottomSheet(
        url: String?,
        missingPlace: String,
        missingDate: String,
        description: String?,
        reporterName : String?
    ){
        if (_binding !=null && isAdded){
            binding.reportInfoLocation.text = missingPlace
            binding.reportInfoDate.text = missingDate
            binding.reportInfoInfoText.text = description
            binding.reportInfoNickNameText.text = reporterName
            url?.let {
                Glide.with(this)
                    .load(it)
                    .apply(
                        RequestOptions().centerCrop()
                            .transform(RoundedCorners(DpUtils.dpToPx(requireContext(), 85)))
                    )
                    .into(binding.reportInfoImg)
            } ?: Log.d("SuspectedBottomSheet", "Image URL is null")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}