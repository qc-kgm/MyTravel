package com.example.mytravel.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytravel.R
import com.example.mytravel.model.City
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.city_recycle_layout.view.*

class AdapterCity(val cities:MutableList<City>) : RecyclerView.Adapter<AdapterCity.ViewholderCity>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderCity {
        return ViewholderCity(
            LayoutInflater.from(parent.context).inflate(R.layout.city_recycle_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewholderCity, position: Int){
        val citys=cities[position]
        holder.view.namecity.text=citys.name
        Picasso.get()
            .load(citys.image)
            .placeholder(R.drawable.hinhnen)
            .error(R.drawable.hinhnen)
            .into(holder.view.imagecity);
    }

    override fun getItemCount(): Int {
    return cities.size
    }
    class ViewholderCity(val view:View):RecyclerView.ViewHolder(view){

    }
}