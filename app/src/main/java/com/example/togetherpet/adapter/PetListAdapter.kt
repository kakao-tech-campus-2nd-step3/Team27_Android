package com.example.togetherpet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.togetherpet.R
import com.example.togetherpet.testData.entity.Missing
import de.hdodenhof.circleimageview.CircleImageView

class PetListAdapter(
    private val context: Context,
    private val missingPetList: List<Missing>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pet = missingPetList[position]
        val viewHolder = holder as ViewHolder
        viewHolder.name.text = pet.missingPetName
        /*//viewHolder.date.text = "D+" + pet.missingDate + "H"
        viewHolder.date.text = context.getString(R.string.sos_missing_date, pet.missingDate)*/
        // pet.missingDate가 String 타입이라면, Int로 변환
        val missingDateInt = pet.missingDate
        viewHolder.date.text = context.getString(R.string.sos_missing_date, missingDateInt)

        viewHolder.place.text = pet.missingPlace
        Glide.with(viewHolder.img.context).load(pet.missingPetImgUrl).into(holder.img)
    }

    override fun getItemCount(): Int = missingPetList.count()
}