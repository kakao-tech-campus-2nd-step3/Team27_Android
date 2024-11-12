package com.example.togetherpet.searching.report.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togetherpet.data.entity.MissingEntity
import com.example.togetherpet.data.entity.ReportEntity
import com.example.togetherpet.data.repository.KakaoLocalRepository
import com.example.togetherpet.databinding.ListMissingPetBinding
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SuspectedAdapter (
    private var suspected: List<ReportEntity>,
    private val kakaoLocalRepository: KakaoLocalRepository,
    private val onItemClicked: (ReportEntity) -> Unit
): RecyclerView.Adapter<SuspectedAdapter.SuspectedViewHolder>(){
    class SuspectedViewHolder(val binding: ListMissingPetBinding):
            RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuspectedAdapter.SuspectedViewHolder {
        val binding =
            ListMissingPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuspectedAdapter.SuspectedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuspectedAdapter.SuspectedViewHolder, position: Int) {
        val suspectedReports = suspected[position]
        holder.binding.apply {
            //이미지
            suspectedReports.imageUrl.firstOrNull()?.let { imageUrl ->
                Log.d("ReportAdapter", "Binding image URL: $imageUrl at position $position")
                Glide.with(holder.itemView.context)
                    .load(Uri.parse(imageUrl))
                    .circleCrop()
                    .into(listMissingPetImg)
            }
            //위도, 경도 -> 주소
            val latLng = LatLng.from(suspectedReports.latitude, suspectedReports.longitude)
            CoroutineScope(Dispatchers.Main).launch {
                val address = withContext(Dispatchers.IO) {
                    kakaoLocalRepository.latLngToAddress(latLng)
                }
                Log.d("ReportAdapter", "Address for position $position: ${address.address?.addressName}")
                listMissingPetAddress.text = address.address?.addressName ?: "주소를 불러올 수 없습니다"
            }

            holder.binding.root.setOnClickListener {
                onItemClicked(suspectedReports)
            }
        }
    }

    override fun getItemCount(): Int = suspected.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateReports(newReports: List<ReportEntity>) {
        Log.d("ReportAdapter", "Updating reports. New size: ${newReports.size}")
        suspected = newReports
        notifyDataSetChanged()
    }
}