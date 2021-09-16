package com.example.mytravel.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.R
import com.example.mytravel.model.City
import com.example.mytravel.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_city_detail.*
import kotlinx.android.synthetic.main.activity_city_detail.view.*
import kotlinx.android.synthetic.main.comment_recycle.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.image_city_layout.*
import org.json.JSONException


class CityDetail : AppCompatActivity() {

//    private var _binding: ActivityCityDetailBinding? =null
    private lateinit var adapterCityDetail: AdapterCityDetail
    private lateinit var layoutManager: LinearLayoutManager
    var wish=0

//    var urls:String=getString(R.string.ip)+"/travelapp/services/getimagecity.php"
    lateinit var urls: String
    var id:Int =0
    var name="Hà Nội"
    var overview=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_detail)
        layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        urls=getString(R.string.ip)+"/travelapp/services/getimagecity.php"
        if(intent.getBundleExtra("bundle") !=null) {
//            val id = intent.getBundleExtra("bundle")!!.getInt("ID")
            val bundle=intent.getBundleExtra("bundle")
            id=bundle!!.getInt("keyID")
            name=bundle!!.getString("nameofcity").toString()
            overview= bundle!!.getString("overview").toString()
            namecity.text=name
            overviewdetail.text=overview
//            textView2.text = id.toString()
            urls=urls+"?q=$id"+"&c=1"
            getdata(urls,id)
            isWish()
            getComment()
        } else {
            val i=intent.getIntExtra("id",1)
            getcity(getString(R.string.ip)+"/travelapp/services/getcity.php?q=$i")
        }
        weather.setOnClickListener {
            val intent2= Intent(this, Weather::class.java)
            intent2.putExtra("city",namecity.text)
            this.startActivity(intent2)
        }
        button_wish.setOnClickListener{
            if (wish==0){
                wish=1
                Picasso.get()
                    .load(R.drawable.heart_like)
                    .into(button_wish)
                addWish()
            }
            else {
                wish=0
                Picasso.get()
                    .load(R.drawable.heart)
                    .into(button_wish)
                rmWish()
            }
        }
        tocomment.setOnClickListener {
            val nd=textcomment.text.toString().trim()
            if(nd.isEmpty()) {
                println("nothing to comment")
            }
            else {
                pushComment()
                textcomment.text.clear()
//                    getComment()
            }
        }
        activities.setOnClickListener {
//            startActivity(Intent(this,MapsActivity::class.java))
            val intent=Intent(this, HoatDong::class.java)
            intent.putExtra("idcity",id)
            startActivity(intent)
        }
        sights.setOnClickListener {
            val intent2=Intent(this, Sights::class.java)
            intent2.putExtra("idcity",id)
            startActivity(intent2)
        }
        restaurent.setOnClickListener {
//            val intent=Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=pizza+seattle+wa"))
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?z=5&q=restaurents+$name"))
            intent.setPackage("com.google.android.apps.maps")
//            (intent.resolveActivity(packageManager)).let {
                startActivity(intent)
//            }
        }
        hotel.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?z=5&q=hotel+$name"))
            intent.setPackage("com.google.android.apps.maps")
//            (intent.resolveActivity(packageManager)).let {
            startActivity(intent)
        }
    }
    fun getdata(urls:String,ids:Int){
//        doAsync {
        var listimg:MutableList<String> = mutableListOf()
        var requestq= Volley.newRequestQueue(this@CityDetail)
        val jsonArrayRequest= JsonArrayRequest(
            Request.Method.GET,urls,null, { response ->
                print("duoc roi")
                run {
//                    Toast.makeText(this@CityDetail,"get data OK", Toast.LENGTH_LONG).show()
                    for (i in 0..response.length()-1) {
                        try {
                            val objects = response.getJSONObject(i)
                            println("get thanh cong link ")
                            listimg.add(
                                objects.getString("link")
                            )
                            println(listimg[i])
                        } catch (error: JSONException) {
                            Log.e("error", "loi get data")
                        }
                    }

                }
                listimg.let {
                    showdetail(it)
                }
            },
            { error ->
                // TODO: Handle error
                Toast.makeText(this@CityDetail,"Loi khi get data ", Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)
//            activityUiThread {
//                listimg.let {
//                    showdetail(it)
//                }
//            }
//        }
    }
    fun isWish(){
        val cate =1
        val userid=User.id
        val cityid=id
        val url= getString(R.string.ip)+"/travelapp/services/getwish.php?id=$cityid&c=$cate&user=$userid"
        val requestq=Volley.newRequestQueue(this)
        val stringRequest=StringRequest(
            Request.Method.GET,
            url,
            {
                response ->
                if (response.contentEquals("true")) {
                    Picasso.get()
                        .load(R.drawable.heart_like)
                        .error(R.drawable.heart)
                        .into(button_wish)
                    wish=1
                }
            },
            {
//                error->
                println("loi get wish city")

            }
        )
        requestq.add(stringRequest)
    }
    fun getComment(){
        val cate =1
        val cityid=id
        val url= getString(R.string.ip)+"/travelapp/services/getcomment.php?id=$cityid&c=$cate"
        val ls_user= mutableListOf<String>()
        val ls_cmt= mutableListOf<String>()
        val requestq=Volley.newRequestQueue(this)
        val jsonArrayRequest=JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            {
                response->
                for (i in 0..response.length()-1){
                    val obj=response.getJSONObject(i)
                    ls_user.add(obj.getString("name"))
                    ls_cmt.add(obj.getString("cmt"))
                }
                showcmt(ls_user,ls_cmt)
            },
            {
                println("loi get comment volley")
            }
        )
        requestq.add(jsonArrayRequest)
    }
    private fun showcmt(ls1:MutableList<String>,ls2:MutableList<String>){
        comment.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        comment.adapter=AdapterComment(ls1,ls2)

    }
    private fun showdetail(ls: MutableList<String>) {
        imagedetail.layoutManager=layoutManager
            adapterCityDetail = AdapterCityDetail(ls)
//        recycleviewcity.layoutManager = LinearLayoutManager(activity)

            imagedetail.adapter = adapterCityDetail
//            imagedetail.smoothScrollToPosition(0)
            adapterCityDetail.notifyDataSetChanged()
//            imagedetail.layoutManager=layoutManager

//            imagedetail.adapter!!.notifyDataSetChanged()

    }
    fun pushComment(){
        val cate =1
        val cityid=id
        val userid=User.id
        val content=textcomment.text.toString().trim()
        val url= getString(R.string.ip)+"/travelapp/services/pushcomment.php"
        val requestq=Volley.newRequestQueue(this)
        val stringRequest=object:StringRequest(
            Request.Method.POST,
            url,
            {
                response->
                if (response.contentEquals("true"))
                {
                    Toast.makeText(
                        this,
                        "Add comment successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                getComment()
            },
            {
                println("loi push comment volley")
                Toast.makeText(
                    this,
                    "Add comment failed! Please try again later",
                    Toast.LENGTH_LONG
                ).show()

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("id_skdd", cityid.toString())
                params.put("cate", cate.toString())
                params.put("userid", userid.toString())
                params.put("content", content)
                return params
            }
        }
        requestq.add(stringRequest)
    }
    private fun addWish(){
        val cate =1
        val cityid=id
        val userid=User.id
        val url= getString(R.string.ip)+"/travelapp/services/addwish.php"
        val requestq=Volley.newRequestQueue(this)
        val stringRequest=object:StringRequest(
            Request.Method.POST,
            url,
            {
                    response->
                if (response.contentEquals("true"))
                {
                    println("add thanh cong wish list")
                }

            },
            {
                println("loi add wish list")

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("id_st", cityid.toString())
                params.put("cate", cate.toString())
                params.put("userid", userid.toString())
                return params
            }
        }
        requestq.add(stringRequest)
    }
    private fun rmWish(){
        val cate =1
        val cityid=id
        val userid=User.id
        val url= getString(R.string.ip)+"/travelapp/services/rmwish.php"
        val requestq=Volley.newRequestQueue(this)
        val stringRequest=object:StringRequest(
            Request.Method.POST,
            url,
            {
                    response->
                if (response.contentEquals("true"))
                {
                    println("delete thanh cong wish list")
                }

            },
            {
                println("loi delete wish list")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("id_st", cityid.toString())
                params.put("cate", cate.toString())
                params.put("userid", userid.toString())
                return params
            }
        }
        requestq.add(stringRequest)
    }

    fun getcity(url: String) {
        var city= City(0,"","","")
        var requestq=Volley.newRequestQueue(this)
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
                id=city.id
                name=city.name
                overview=city.overview
                namecity.text=name
                overviewdetail.text=overview
//            textView2.text = id.toString()
                urls=urls+"?q=$id"+"&c=1"
                getdata(urls,id)
                isWish()
                getComment()
            },
            { error ->
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)
    }
}
