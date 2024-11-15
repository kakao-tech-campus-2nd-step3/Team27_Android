package com.jnu.togetherpet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jnu.togetherpet.R
import com.jnu.togetherpet.databinding.SearchingBtnListBinding
import com.jnu.togetherpet.ui.fragment.searching.enums.ButtonType

class SearchingBtnListAdapter(
    //missingStatusKey 상태
    private val isPetMissing: Boolean,
    private val petName: String? = null,
    private val onItemClick: (ButtonType) -> Unit
) : RecyclerView.Adapter<SearchingBtnListAdapter.ResearchingBtnViewHolder>() {

    //기본 데이터
    private val defaultItem = listOf(ButtonType.MISSING, ButtonType.REPORT)

    //MISSING 버튼 선택된 상태로 설정
    private var selectedPosition: Int = 0

    class ResearchingBtnViewHolder(val binding: SearchingBtnListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(buttonType: ButtonType, isSelected: Boolean) {
            binding.researchingBtn.text = buttonType.displayText
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
            val petButton = petName?.let { ButtonType.MyPET.setPetName(it) } ?: ButtonType.MyPET
            defaultItem + petButton
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