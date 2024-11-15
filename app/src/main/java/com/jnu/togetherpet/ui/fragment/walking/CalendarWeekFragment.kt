package com.jnu.togetherpet.ui.fragment.walking

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.jnu.togetherpet.databinding.FragmentCalendarWeekBinding
import java.time.LocalDate

class CalendarWeekFragment : Fragment() {

    private var _binding : FragmentCalendarWeekBinding? = null
    private val binding get() = _binding!!
    private lateinit var textViewList: List<TextView>
    private lateinit var dates: List<LocalDate>

    private var position: Int = 0

    private val todayPosition = Int.MAX_VALUE / 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarWeekBinding.inflate(inflater)
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstance: Bundle?){
        super.onViewCreated(view, savedInstance)
        val newDate = calculateNewDate()
        Log.d("testt", "newDate = $newDate")
        calculateDatesOfWeek(newDate)
        setOneWeekDateIntoTextView()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        resetUi()
    }

    private fun initViews() {
        with(binding) {
            textViewList = listOf(
                dateItemOne, dateItemTwo, dateItemThree, dateItemFour, dateItemFive, dateItemSix, dateItemSeven
            )
        }

        arguments?.let{
            position = it.getInt("position")
        }
    }

    private fun calculateNewDate(): LocalDate {
        val curDate = LocalDate.now()
        Log.d("testt", "curDate = $curDate")
        return if (position < todayPosition){
            curDate.minusDays(((todayPosition - position) * 7).toLong())
        } else if (position > todayPosition) {
            curDate.plusDays(((position - todayPosition) * 7).toLong())
        } else {
            curDate
        }
    }


    private fun setOneWeekDateIntoTextView() {
         textViewList.forEachIndexed() { index, i ->
            setDay(i, dates[index])
        }
    }

    private fun setDay(textView: TextView, date: LocalDate) {
        Log.d("testt", "${date.toString()}")
        textView.text = date.toString().substring(8..9)
        if (LocalDate.now() == date) setSelectedDate(textView)
        textView.setOnClickListener{
            resetUi()
            setFragmentResult("dateClick", bundleOf("date" to date.toEpochDay()))
            setSelectedDate(textView)
        }
    }

    private fun resetUi() {
        for (i in textViewList.indices) {
            textViewList[i].setTextColor(Color.BLACK)
            textViewList[i].setTypeface(textViewList[i].typeface, Typeface.BOLD)
        }
    }

    private fun setSelectedDate(textView: TextView) {
        textView.setTextColor(Color.BLUE)
        textView.setTypeface(textView.typeface, Typeface.BOLD)
    }

    private fun calculateDatesOfWeek(today: LocalDate) {
        val dateList = ArrayList<LocalDate>()
        val dayOfToday = today.dayOfWeek.value
        Log.d("testt", "$dayOfToday")
        Log.d("testt", "Today = $today")

        if (dayOfToday == SUNDAY) {
            for (day in MONDAY..SUNDAY) {
                dateList.add(today.plusDays((day - 1).toLong()))
            }
        } else {
            for (day in (MONDAY - 1) until  dayOfToday) {
                dateList.add(today.minusDays((dayOfToday - day).toLong()))
            }
            for (day in dayOfToday .. (SUNDAY - 1)) {
                dateList.add(today.plusDays((day - dayOfToday).toLong()))
            }
        }
        Log.d("testt", dateList.toString())
        this.dates =  dateList
    }

    companion object {
        fun newInstance(position: Int): CalendarWeekFragment {
            val fragment = CalendarWeekFragment().apply{
                arguments = bundleOf("position" to position)
            }
            return fragment
        }
        const val MONDAY = 1
        const val SUNDAY = 7
    }
}
