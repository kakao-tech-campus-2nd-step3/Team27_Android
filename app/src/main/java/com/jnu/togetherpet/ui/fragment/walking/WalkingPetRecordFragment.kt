package com.jnu.togetherpet.ui.fragment.walking

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jnu.togetherpet.ui.adapter.CalendarViewPagerAdapter
import com.jnu.togetherpet.ui.adapter.OnClickWalkingRecordListener
import com.jnu.togetherpet.R
import com.jnu.togetherpet.ui.adapter.WalkingRecordAdapter
import com.jnu.togetherpet.databinding.FragmentWalkingPetRecordBinding
import com.jnu.togetherpet.ui.viewmodel.walking.WalkingPetRecordViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale

@AndroidEntryPoint
class WalkingPetRecordFragment : Fragment(), OnClickWalkingRecordListener {

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
        val calendarAdapter = CalendarViewPagerAdapter(requireActivity())
        binding.weekViewpager
        binding.weekViewpager.adapter = calendarAdapter
        binding.weekViewpager.setCurrentItem(CalendarViewPagerAdapter.START_POSITION, false)
        binding.calendarDateText.text = selectedDate.toString()
        sharedViewModel.getRecordToLocal(selectedDate)

    }

    fun initListener() {
        binding.walkingPageButton.setOnClickListener {
            navigateToWalkingPage()
        }

        setFragmentResultListener("dateClick") { key, bundle ->
            val date = bundle.getLong("date")
            binding.calendarDateText.text = LocalDate.ofEpochDay(date).toString()
            selectedDate = LocalDate.ofEpochDay(date)
            Log.d("testt", "fragmentResultListener : $selectedDate")
            sharedViewModel.getRecordToLocal(selectedDate)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.arrayRecord.collectLatest {
                    Log.d("testt", "submit $it")
                    walkingRecyclerViewAdapter.submitList(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.allDistance.collectLatest {
                    if (it == 0L) binding.distanceSumValue.text = "오늘은 산책을 안했어요."
                    else binding.distanceSumValue.text = "총 ${it.toString()}m 산책했어요!"
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.allTime.collectLatest {
                    if (it == 0L) binding.timeSumValue.text = "오늘은 산책을 안했어요."
                    else {
                        val format = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)
                        format.timeZone = TimeZone.getTimeZone("UTC")
                        binding.timeSumValue.text = format.format(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.selectDay.collectLatest {
                    sharedViewModel.getRecordToLocal(selectedDate)
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

}