package com.jnu.togetherpet.ui.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jnu.togetherpet.data.entity.MissingEntity
import com.jnu.togetherpet.data.repository.KakaoLocalRepository
import com.jnu.togetherpet.databinding.ListMissingPetBinding
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MissingAdapter(
    private var missings: List<MissingEntity>,
    private val kakaoLocalRepository: KakaoLocalRepository,
    private val onItemClicked: (MissingEntity) -> Unit
) :
    RecyclerView.Adapter<MissingAdapter.MissingViewHolder>() {

    class MissingViewHolder(val binding: ListMissingPetBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissingViewHolder {
        val binding =
            ListMissingPetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MissingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MissingViewHolder, position: Int) {
        val missing = missings[position]
        holder.binding.apply {
            //이미지
            missing.petImageUrl.firstOrNull()?.let { imageUrl ->
                Log.d("ReportAdapter", "Binding image URL: $imageUrl at position $position")
                Glide.with(holder.itemView.context)
                    .load(Uri.parse(imageUrl))
                    .circleCrop()
                    .into(listMissingPetImg)
            }
            //위도, 경도 -> 주소
            val latLng = LatLng.from(missing.latitude, missing.longitude)
            CoroutineScope(Dispatchers.Main).launch {
                val address = withContext(Dispatchers.IO) {
                    kakaoLocalRepository.latLngToAddress(latLng)
                }
                Log.d("ReportAdapter", "Address for position $position: ${address.address?.addressName}")
                listMissingPetAddress.text = address.address?.addressName ?: "주소를 불러올 수 없습니다"
            }

            holder.binding.root.setOnClickListener {
                onItemClicked(missing)
            }
        }
    }

    override fun getItemCount(): Int = missings.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateReports(newReports: List<MissingEntity>) {
        Log.d("ReportAdapter", "Updating reports. New size: ${newReports.size}")
        missings = newReports
        notifyDataSetChanged()
    }
}