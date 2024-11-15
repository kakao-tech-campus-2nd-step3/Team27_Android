package com.jnu.togetherpet.ui.adapter

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jnu.togetherpet.databinding.WalkingRecordItemBinding
import com.jnu.togetherpet.extensions.drawLine
import com.jnu.togetherpet.extensions.formattingLocalDateTimeToString
import com.jnu.togetherpet.data.entity.WalkingRecord
import com.kakao.vectormap.GestureType
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import java.util.Locale

class WalkingRecordAdapter(
    private val listener: OnClickWalkingRecordListener
) : ListAdapter<WalkingRecord, WalkingRecordViewHolder>(WalkingRecordDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkingRecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WalkingRecordItemBinding.inflate(inflater, parent, false)
        return WalkingRecordViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: WalkingRecordViewHolder, position: Int) {
        val walkingRecord = getItem(position)
        holder.bind(walkingRecord)
    }

}

class WalkingRecordDiffCallBack : DiffUtil.ItemCallback<WalkingRecord>() {
    override fun areItemsTheSame(oldItem: WalkingRecord, newItem: WalkingRecord): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: WalkingRecord, newItem: WalkingRecord): Boolean {
        return oldItem.date == newItem.date && oldItem.time == newItem.time
    }
}

class WalkingRecordViewHolder(
    private val binding: WalkingRecordItemBinding,
    private val listener: OnClickWalkingRecordListener
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            listener.navigateToDetailRecordPage(bindingAdapterPosition)
        }
    }

    fun bind(walkingRecord: WalkingRecord) {
        binding.distanceValueItem.text = "총 ${walkingRecord.distance}m 산책했어요!"
        val time = formattingLongToTime(walkingRecord.time)
        binding.timeValueItem.text = time
        binding.timeResultRedText.text =
            "${walkingRecord.startTime.formattingLocalDateTimeToString()} ~ ${walkingRecord.endTime.formattingLocalDateTimeToString()}"
        initMap(walkingRecord)
    }

    private fun initMap(walkingRecord: WalkingRecord) {
        val map = binding.walkingMapView
        map.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                Log.d("testt", "MapDestroy")
            }

            override fun onMapError(error: Exception) {
                Log.d("testt", error.message.toString())
                //에러 처리
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("testt", "MapReady")
                kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(walkingRecord.route.first()))
                kakaoMap.drawLine(walkingRecord.route)
                kakaoMap.setGestureEnable(GestureType.Zoom, false)
                kakaoMap.setGestureEnable(GestureType.TwoFingerSingleTap, false)
                kakaoMap.setGestureEnable(GestureType.OneFingerDoubleTap, false)
                kakaoMap.setGestureEnable(GestureType.OneFingerZoom, false)
                kakaoMap.setGestureEnable(GestureType.Pan, false)
                kakaoMap.setGestureEnable(GestureType.Tilt, false)
                kakaoMap.setGestureEnable(GestureType.Rotate, false)
                kakaoMap.setOnMapClickListener { _, _, _, _ ->
                    listener.navigateToDetailRecordPage(bindingAdapterPosition)
                }
            }

        })

    }

    private fun formattingLongToTime(time: Long): String {
        val format = SimpleDateFormat("HH:mm:ss", Locale.KOREAN)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(time)
    }
}

interface OnClickWalkingRecordListener {
    fun navigateToDetailRecordPage(position: Int)
}