package com.example.mytravel.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytravel.R
import com.example.mytravel.model.Sight
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.city_recycle_layout.view.*

class AdapterSight (val sights:MutableList<Sight>) : RecyclerView.Adapter<AdapterSight.ViewholderSight>() {



    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewholderSight {
        view= LayoutInflater.from(parent.context).inflate(R.layout.city_recycle_layout,parent,false)
        val holder= ViewholderSight(view)
//        holder.itemView.setOnClickListener {
//            val inte=Intent(it.context,CityDetail::class.java)
//            it.context.startActivity(inte)
//        }
//        holder.itemView.onClick
        return holder


    }

    override fun onBindViewHolder(holder: ViewholderSight, position: Int){
        val sight:Sight=sights[position]
        holder.view.namecity.text=sight.name
        Picasso.get()
            .load(sight.image)
            .placeholder(R.drawable.hinhnen)
            .error(R.drawable.hinhnen)
            .into(holder.view.imagecity);
//        holder.setID(citys.id)

//        holder.itemView.setOnClickListener {
////            val intent=Intent(view.context, CityDetail::class.java)
////            view.context.startActivity(intent)
//            Log.v("test","da click thanh cong")
//        }
        holder.view.imagecity.setOnClickListener {
            println("click into image")

            val intent= Intent(view.context, SightDetail::class.java)
            val bundle= Bundle()
            bundle.putInt("keyID",sight.id)
            bundle.putString("nameofsight",sight.name)
            bundle.putString("overview",sight.overview)
            bundle.putString("opentime",sight.opentime)
            intent.putExtra("bundle",bundle)
//            intent.putExtra("keyID",citys.id)
//            intent.putExtra("nameofcity",citys.name)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return sights.size
    }
    class ViewholderSight(val view: View): RecyclerView.ViewHolder(view){
        //        val v:View =view
//        var id :Int ?= null
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
//        fun setID(i:Int){
//            this.id=i
//        }

    }
}