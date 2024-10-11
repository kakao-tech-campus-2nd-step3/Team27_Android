package com.example.togetherpet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togetherpet.R
import com.example.togetherpet.databinding.ListMissingPetBinding
import com.example.togetherpet.testData.entity.Missing
import de.hdodenhof.circleimageview.CircleImageView

class PetListAdapter(
    private val context: Context,
    private val missingPetList: List<Missing>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(val binding: ListMissingPetBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.listMissingPetName
        val date: TextView = binding.listMissingPetDate
        val place: TextView = binding.listMissingPetAddress
        val img: CircleImageView = binding.listMissingPetImg
    }

    //Create new views
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListMissingPetBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    //Replace the contents of a view
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pet = missingPetList[position]
        val viewHolder = holder as ViewHolder
        viewHolder.name.text = pet.missingPetName

        val missingDateInt = pet.missingDate
        viewHolder.date.text = context.getString(R.string.sos_missing_date, missingDateInt)

        viewHolder.place.text = pet.missingPlace
        Glide.with(viewHolder.img.context).load(pet.missingPetImgUrl).into(holder.img)
    }

    override fun getItemCount(): Int = missingPetList.count()
}