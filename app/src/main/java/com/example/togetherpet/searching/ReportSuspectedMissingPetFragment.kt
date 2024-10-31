package com.example.togetherpet.searching

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.togetherpet.databinding.ReportSuspectedMissingPetFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReportSuspectedMissingPetFragment : Fragment() {
    private lateinit var _binding: ReportSuspectedMissingPetFragmentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReportSuspectedMissingPetFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}