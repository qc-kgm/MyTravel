package com.example.mytravel.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytravel.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_city_detail.view.*
import kotlinx.android.synthetic.main.image_city_layout.view.*

class AdapterCityDetail(val lsimage:MutableList<String>) : RecyclerView.Adapter<AdapterCityDetail.ViewholderCityDetail>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderCityDetail {
        return (
                ViewholderCityDetail(LayoutInflater.from(parent.context).inflate(R.layout.image_city_layout,parent,false))
                )
    }

    override fun onBindViewHolder(holder: ViewholderCityDetail, position: Int) {
//        TODO("Not yet implemented")
        var linkimg=lsimage[position]
        Picasso.get()
            .load(linkimg)
            .placeholder(R.drawable.hinhnen)
            .error(R.drawable.hinhnen)
            .into(holder.view.citydetails);
//        holder.view.namecity.text=n
//        holder.view.overviewdetail.text=o
//        holder.view.overviewdetail.text="Thanh pho Ha Noi"

//        holder.view.weather.setOnClickListener {
//            val intent= Intent(holder.view.context, Weather::class.java)
//            intent.putExtra("keyID",holder.view.namecity.text)
//            holder.view.context.startActivity(intent)
//        }
    }

    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return lsimage.size

    }

    class ViewholderCityDetail(val view: View):RecyclerView.ViewHolder(view){

    }

}