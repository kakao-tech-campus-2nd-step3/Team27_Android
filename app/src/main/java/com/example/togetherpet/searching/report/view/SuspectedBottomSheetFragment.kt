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
import com.example.togetherpet.data.repository.KakaoLocalRepository
import com.example.togetherpet.databinding.ReportInfoBottomSheetBinding
import com.example.togetherpet.searching.report.viewModel.ReportDataViewModel
import com.example.togetherpet.utils.DpUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        val reportId = arguments?.getLong(ARG_REPORT_ID) ?: error("Report ID 없음")
        observeReportDetails(reportId)
        return binding.root
    }

    private fun observeReportDetails(reportId: Long) {
        viewLifecycleOwner.lifecycleScope.launch {
            reportDataViewModel.suspectedDetail.collectLatest { detail ->
                Log.d("MissingBottomSheetFragment", "Received detail: $detail")
                detail?.let {
                    updateBottomSheet(
                        description = it.description,
                        missingPlace = "${it.latitude}, ${it.longitude}",
                        missingDate = it.foundDate!!,
                        uri = it.imageUrl.toString(),
                        reporterName = it.reporterName
                    )
                }
            }
        }
    }

    private fun updateBottomSheet(
        description: String?,
        missingPlace: String,
        missingDate: String,
        uri: String?,
        reporterName : String?
    ){
        if (_binding !=null && isAdded){
            binding.reportInfoLocation.text = missingPlace
            binding.reportInfoDate.text = missingDate
            binding.reportInfoInfoText.text = description
            binding.reportInfoInfoText.text = reporterName
            Glide.with(this)
                .load(uri)
                .apply(
                    RequestOptions().centerCrop()
                        .transform(RoundedCorners(DpUtils.dpToPx(requireContext(), 85)))
                )
                .into(binding.reportInfoImg)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}