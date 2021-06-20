package com.example.mytravel.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytravel.R
import com.example.mytravel.model.City
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.city_recycle_layout.view.*

class AdapterCity(val cities:MutableList<City>) : RecyclerView.Adapter<AdapterCity.ViewholderCity>() {



    lateinit var view:View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderCity {
        view= LayoutInflater.from(parent.context).inflate(R.layout.city_recycle_layout,parent,false)
        val holder= ViewholderCity(view)
//        holder.itemView.setOnClickListener {
//            val inte=Intent(it.context,CityDetail::class.java)
//            it.context.startActivity(inte)
//        }
//        holder.itemView.onClick
        return holder


    }

    override fun onBindViewHolder(holder: ViewholderCity, position: Int){
        val citys=cities[position]
        holder.view.namecity.text=citys.name
        Picasso.get()
            .load(citys.image)
            .placeholder(R.drawable.hinhnen)
            .error(R.drawable.hinhnen)
            .into(holder.view.imagecity);
        holder.setID(citys.id)

//        holder.itemView.setOnClickListener {
////            val intent=Intent(view.context, CityDetail::class.java)
////            view.context.startActivity(intent)
//            Log.v("test","da click thanh cong")
//        }
        holder.view.imagecity.setOnClickListener {
            println("click into image")

            val intent=Intent(view.context, CityDetail::class.java)
            val bundle=Bundle()
            bundle.putInt("keyID",citys.id)
            bundle.putString("nameofcity",citys.name)
            bundle.putString("overview",citys.overview)
            intent.putExtra("bundle",bundle)
//            intent.putExtra("keyID",citys.id)
//            intent.putExtra("nameofcity",citys.name)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
    return cities.size
    }
    class ViewholderCity(val view:View):RecyclerView.ViewHolder(view){
//        val v:View =view
        var id :Int ?= null
//        var con:Context
//        init {
////            view.setOnClickListener(this)
////            itemView.isClickable=true
//            con=view.context
//            view.setOnClickListener(this)
//        }
//        override fun onClick(v: View?) {
//            println("onclickkkkkkkk")
////            val context = itemView.context
//            val intentdetail = Intent(con, CityDetail::class.java)
////            val bundle = Bundle()
////            bundle.putInt("ID", id!!)
////            intentdetail.putExtra("bundle", bundle)
//            con.startActivity(intentdetail)
//
//
//        }
        fun setID(i:Int){
            this.id=i
        }

    }
}