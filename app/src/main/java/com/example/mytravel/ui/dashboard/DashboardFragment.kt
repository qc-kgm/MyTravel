package com.example.mytravel.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.R
import com.example.mytravel.databinding.FragmentDashboardBinding
import com.example.mytravel.model.City
import com.example.mytravel.model.User
import com.example.mytravel.ui.home.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.city_recycle_layout.view.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.json.JSONException

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var url:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        url=getString(R.string.ip)+"/travelapp/services/getwishlist.php?q=${User.id}"
        getData(url)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getData( url:String){
//        listCity.clear()
        var list:MutableList<WishMember> = mutableListOf()
        var requestq= Volley.newRequestQueue(activity)
        val jsonArrayRequest= JsonArrayRequest(
            Request.Method.GET,url,null, { response ->
                print("duoc roi")
                run {
//                    Toast.makeText(activity,"get data OK",Toast.LENGTH_LONG).show()
                    for (i in 0..response.length()-1) {
                        try {
                            val objects = response.getJSONObject(i)
                            list.add(
                                WishMember(
                                    objects.getInt("cate"),
                                    objects.getInt("id_st"),
                                    objects.getString("image"),
                                    objects.getString("name")
                                )
                            )
                            println(i)
                        } catch (error: JSONException) {
                            Log.e("error", "loi get data")
                        }
                    }

                }
                list.let {
                    show(it)
                }
            },
            { error ->
                // TODO: Handle error
                Toast.makeText(activity,"Loi khi get data ", Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)

    }
    fun show(ls:MutableList<WishMember>){
        wish_list.adapter=AdapterWishlist(ls)
        wish_list.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
    }
}
data class WishMember(
    val category:Int,
    val Id_st:Int,
    val link_image:String,
    val name:String
)
class AdapterWishlist(val ls:MutableList<WishMember>) :RecyclerView.Adapter<AdapterWishlist.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.city_recycle_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i=ls[position]
        holder.view.namecity.text=i.name
        Picasso.get()
            .load(i.link_image)
            .error(R.drawable.hinhnen)
            .into(holder.view.imagecity)
        holder.view.imagecity.setOnClickListener {
            println("click into image")
            when (i.category) {
                1 -> {
                    val intent= Intent(holder.view.context, CityDetail::class.java)
                    intent.putExtra("id",i.Id_st)
//                    val city=getcity("192.168.1.7/travelapp/services/getcity.php?q=${i.Id_st}",holder.view.context)
//                    val bundle=Bundle()
//                    bundle.putInt("keyID",city.id)
//                    bundle.putString("nameofcity",city.name)
//                    bundle.putString("overview",city.overview)
//                    intent.putExtra("bundle",bundle)
//            intent.putExtra("keyID",citys.id)
//            intent.putExtra("nameofcity",citys.name)
                    holder.view.context.startActivity(intent)
                }
                2 -> {
                    val intent= Intent(holder.view.context, HoatDongDetail::class.java)
                    intent.putExtra("id",i.Id_st)
                    holder.view.context.startActivity(intent)
                    println("start hoatdong")

                }
                3 -> {
                    val intent= Intent(holder.view.context, SightDetail::class.java)
                    intent.putExtra("id",i.Id_st)
                    holder.view.context.startActivity(intent)
                    println("start sight")
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return ls.size
    }
    class ViewHolder(val view:View) : RecyclerView.ViewHolder(view){

    }
    fun getcity(url: String,context: Context): City {
        var city=City(0,"","","")
            var requestq=Volley.newRequestQueue(context)
            val jsonArrayRequest=JsonArrayRequest(
                Request.Method.GET,url,null, { response ->
                    print("duoc roi")
                    run {
                            try {
                                val objects = response.getJSONObject(0)
                                city= City(
                                        objects.getInt("id"),
                                        objects.getString("city"),
                                        objects.getString("image"),
                                        objects.getString("overview")
                                    )
                            } catch (error: JSONException) {
                                Log.e("error", "loi get data")
                            }
                    }
                },
                { error ->
                    error.printStackTrace()
                })
            requestq.add(jsonArrayRequest)
            return city
    }
    fun getsight(){

    }
    fun getActivity(){

    }
}