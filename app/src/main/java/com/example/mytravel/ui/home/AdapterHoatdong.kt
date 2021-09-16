package com.example.mytravel.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytravel.R
import com.example.mytravel.model.Hoatdong
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.city_recycle_layout.view.*

class AdapterHoatdong (val ls:MutableList<Hoatdong>):RecyclerView.Adapter<AdapterHoatdong.ViewHolderHoatdong>() {

    class ViewHolderHoatdong(val v:View):RecyclerView.ViewHolder(v){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterHoatdong.ViewHolderHoatdong {
        return ViewHolderHoatdong(LayoutInflater.from(parent.context).inflate(R.layout.city_recycle_layout,parent,false))
    }

    override fun onBindViewHolder(holder: AdapterHoatdong.ViewHolderHoatdong, position: Int) {
        val hd=ls[position]
        Picasso.get()
            .load(hd.image)
            .error(R.drawable.hinhnen)
            .into(holder.v.imagecity)

        holder.v.namecity.text=hd.Name
        holder.v.imagecity.setOnClickListener {
            val it= Intent(holder.v.context,HoatDongDetail::class.java)
            val bundle=Bundle()
            bundle.putInt("keyID",hd.Id)
            bundle.putString("nameofhd",hd.Name)
            bundle.putString("overview",hd.overview)
            bundle.putString("start_time",hd.start_time)
            bundle.putString("end_time",hd.end_time)
            bundle.putString("location",hd.location)
            it.putExtra("bundle",bundle)
//            intent.putExtra("keyID",citys.id)
//            intent.putExtra("nameofcity",citys.name)
            holder.v.context.startActivity(it)
        }
    }

    override fun getItemCount(): Int {
        return ls.size
    }
}