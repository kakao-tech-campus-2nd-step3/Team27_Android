package com.example.togetherpet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togetherpet.R
import com.example.togetherpet.testData.entity.Missing
import de.hdodenhof.circleimageview.CircleImageView

class PetListAdapter(private val missingPetList: List<Missing>) :
    RecyclerView.Adapter<PetListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.list_missingPet_name)
        val date: TextView = view.findViewById(R.id.list_missingPet_date)
        val place: TextView = view.findViewById(R.id.list_missingPet_address)
        val img: CircleImageView = view.findViewById(R.id.list_missingPet_img)
    }

    //Create new views
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.list_missing_pet, viewGroup, false)
        return ViewHolder(view)
    }

    //Replace the contents of a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pet = missingPetList[position]
        holder.name.text = pet.missingPetName
        holder.date.text = "D+" + pet.missingDate + "H"
        holder.place.text = pet.missingPlace
        Glide.with(holder.img.context).load(pet.missingPetImgUrl).into(holder.img)
    }

    override fun getItemCount(): Int = missingPetList.size
}