package com.example.togetherpet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.togetherpet.R
import com.example.togetherpet.databinding.SearchingBtnListBinding

class SearchingBtnListAdapter(
    //missingStatusKey 상태
    private val isPetMissing: Boolean,
    private val petName: String? = null,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchingBtnListAdapter.ResearchingBtnViewHolder>() {

    //기본 데이터
    private val defaultItem = listOf("실종 정보", "제보 정보")

    private var selectedPosition: Int = 0

    class ResearchingBtnViewHolder(val binding: SearchingBtnListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, isSelected: Boolean) {
            binding.researchingBtn.text = item
            binding.researchingBtn.setBackgroundResource(
                if (isSelected) R.drawable.researching_list_btn_select else R.drawable.researching_list_btn
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResearchingBtnViewHolder {
        val binding =
            SearchingBtnListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ResearchingBtnViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResearchingBtnViewHolder, position: Int) {
        val itemToShow = if (isPetMissing) {
            defaultItem + (petName ?: "반려견 이름 없음")
        } else {
            defaultItem
        }
        holder.bind(itemToShow[position], position == selectedPosition)

        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.absoluteAdapterPosition

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onItemClick(itemToShow[position])
        }
    }

    override fun getItemCount(): Int {
        return if (isPetMissing) {
            defaultItem.size + 1
        } else {
            defaultItem.size
        }
    }
}