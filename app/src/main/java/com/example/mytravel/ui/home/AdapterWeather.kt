package com.example.mytravel.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytravel.R
import com.example.mytravel.model.WeatherInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.weather_recycle.view.*

class AdapterWeather(val lsweatherinfo:MutableList<WeatherInfo>):RecyclerView.Adapter<AdapterWeather.ViewholderWeather>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderWeather {
//        TODO("Not yet implemented")
        return ViewholderWeather(LayoutInflater.from(parent.context).inflate(R.layout.weather_recycle,parent,false))
    }

    override fun onBindViewHolder(holder: ViewholderWeather, position: Int) {
//        TODO("Not yet implemented"
        val ex=lsweatherinfo[position]
        holder.view.namestate.text=ex.state
        holder.view.temp.text=ex.temp.toString()
        holder.view.rangetemp.text=ex.min_temp.toString()+"\u00B0C "+"/ "+ex.max_temp.toString()+"\u00B0C"
        holder.view.date.text=ex.date
        holder.view.wind.text=ex.wind_speed.toString()+" km/h"
        holder.view.humidity.text=" "+ex.humidity.toString()+" "
//        when(ex.weather_state_abbr){
//            "hr"-> {
                Picasso.get()
                    .load("https://www.metaweather.com/static/img/weather/png/${ex.weather_state_abbr}.png")
                    .into(holder.view.state)
//            }

//        }


    }

    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
       return lsweatherinfo.size
    }

    class ViewholderWeather(val view: View):RecyclerView.ViewHolder(view){

    }

}