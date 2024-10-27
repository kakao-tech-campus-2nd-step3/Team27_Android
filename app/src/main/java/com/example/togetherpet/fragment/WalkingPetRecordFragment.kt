package com.example.togetherpet.fragment

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.togetherpet.adapter.CalendarViewPagerAdapter
import com.example.togetherpet.adapter.OnClickWalkingRecordListener
import com.example.togetherpet.R
import com.example.togetherpet.adapter.WalkingRecordAdapter
import com.example.togetherpet.databinding.FragmentWalkingPetRecordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale

@AndroidEntryPoint
class WalkingPetRecordFragment : Fragment(), OnClickWalkingRecordListener, DateClickListener {

    private var _binding: FragmentWalkingPetRecordBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: WalkingPetRecordViewModel by activityViewModels()

    private var selectedDate: LocalDate = LocalDate.now()
    lateinit var walkingRecyclerViewAdapter: WalkingRecordAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWalkingPetRecordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("testt", "onViewCreated")

        initListener()
        initRecyclerView()
        setOneWeekViewPager()
    }

    private fun setOneWeekViewPager() {
        val calendarAdapter = CalendarViewPagerAdapter(requireActivity(), this)
        binding.weekViewpager
        binding.weekViewpager.adapter = calendarAdapter
        binding.weekViewpager.setCurrentItem(CalendarViewPagerAdapter.START_POSITION, false)
        sharedViewModel.getRecord(selectedDate)

    }

    fun initListener() {
        binding.walkingPageButton.setOnClickListener {
            navigateToWalkingPage()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.arrayRecord.collect {
                    walkingRecyclerViewAdapter.submitList(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.allDistance.collect {
                    binding.distanceSumValue.text = "총 ${it.toString()}m 산책했어요!"
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.allTime.collect {
                    val format = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)
                    format.timeZone = TimeZone.getTimeZone("UTC")
                    binding.timeSumValue.text = format.format(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.selectDay.collect {
                    sharedViewModel.getRecord(selectedDate)
                }
            }
        }
    }

    fun initRecyclerView() {
        walkingRecyclerViewAdapter = WalkingRecordAdapter(this)
        binding.walkingRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.walkingRecyclerview.adapter = walkingRecyclerViewAdapter

    }

    fun navigateToWalkingPage() {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_frameLayout, WalkingPetFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun navigateToDetailRecordPage(position: Int) {
        sharedViewModel.getSelectedDetailRecord(position)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.home_frameLayout, WalkingPetRecordDetailFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClickDate(date: LocalDate) {
        selectedDate = date
        sharedViewModel.getRecord(selectedDate)
    }
}