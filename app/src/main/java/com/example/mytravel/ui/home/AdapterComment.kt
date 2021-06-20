package com.example.mytravel.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytravel.R
import kotlinx.android.synthetic.main.comment_recycle.view.*

class AdapterComment(private val list_user_name:MutableList<String>, private val list_content:MutableList<String>): RecyclerView.Adapter<AdapterComment.viewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
//        TODO("Not yet implemented")
        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.comment_recycle,parent,false))
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
//        TODO("Not yet implemented")
        var username=list_user_name[position]
        var comment=list_content[position]
        holder.view.user.text=username
        holder.view.noidung.text=comment
    }

    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return list_content.size
    }
    class viewHolder(val view:View):RecyclerView.ViewHolder(view){

    }
}