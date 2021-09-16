package com.example.mytravel.ui.home

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
import com.example.mytravel.model.Hoatdong
import com.example.mytravel.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_hoat_dong_detail.*
import org.json.JSONException

class HoatDongDetail : AppCompatActivity() {
    var id=0
    var wish=0
    var name=""
    var overview=""
    var _location=""
    var start_time=""
    var end_time=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hoat_dong_detail)


        if (intent.getBundleExtra("bundle") !=null) {
            val bundle=intent.getBundleExtra("bundle")
            id=bundle!!.getInt("keyID")
            name=bundle!!.getString("nameofhd").toString()
            overview= bundle!!.getString("overview").toString()
            start_time=bundle.getString("start_time").toString()
            end_time=bundle.getString("end_time").toString()
            _location=bundle.getString("location").toString()
            namecity.text=name
            overviewdetail.text=overview
            location.text= "Location : $_location"
            starttime.text="From $start_time To $end_time"
            val urls=getString(R.string.ip) +"/travelapp/services/getimagecity.php?q=$id&c=2"
            getImage(urls)
            isWish()
            getComment()


        }
        else {

            val i=intent.getIntExtra("id",1)
            gethoatdong(getString(R.string.ip)+"/travelapp/services/gethoatdong.php?q=$i")
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


    }




    fun getImage(urls:String){
//        doAsync {
        var listimg:MutableList<String> = mutableListOf()
        var requestq= Volley.newRequestQueue(this)
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
                    showimage(it)
                }
            },
            { error ->
                // TODO: Handle error
//                Toast.makeText(this,"Loi khi get data ", Toast.LENGTH_LONG).show()
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
        val cate =2
        val userid= User.id
        val sightid=id
        val url= getString(R.string.ip)+"/travelapp/services/getwish.php?id=$sightid&c=$cate&user=$userid"
        val requestq= Volley.newRequestQueue(this)
        val stringRequest= StringRequest(
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
                println("loi get wish sight")

            }
        )
        requestq.add(stringRequest)
    }
    fun getComment(){
        val cate =2
        val sightid=id
        val url= getString(R.string.ip)+"/travelapp/services/getcomment.php?id=$sightid&c=$cate"
        val ls_user= mutableListOf<String>()
        val ls_cmt= mutableListOf<String>()
        val requestq= Volley.newRequestQueue(this)
        val jsonArrayRequest= JsonArrayRequest(
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
        comment.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        comment.adapter=AdapterComment(ls1,ls2)

    }

    fun pushComment(){
        val cate =2
        val sightid=id
        val userid= User.id
        val content=textcomment.text.toString().trim()
        val url= getString(R.string.ip)+"/travelapp/services/pushcomment.php"
        val requestq= Volley.newRequestQueue(this)
        val stringRequest=object: StringRequest(
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
                params.put("id_skdd", sightid.toString())
                params.put("cate", cate.toString())
                params.put("userid", userid.toString())
                params.put("content", content)
                return params
            }
        }
        requestq.add(stringRequest)
    }
    private fun addWish(){
        val cate =2
        val sightid=id
        val userid= User.id
        val url= getString(R.string.ip)+"/travelapp/services/addwish.php"
        val requestq= Volley.newRequestQueue(this)
        val stringRequest=object: StringRequest(
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
                params.put("id_st", sightid.toString())
                params.put("cate", cate.toString())
                params.put("userid", userid.toString())
                return params
            }
        }
        requestq.add(stringRequest)
    }
    private fun rmWish(){
        val cate =2
        val sightid=id
        val userid= User.id
        val url= getString(R.string.ip)+"/travelapp/services/rmwish.php"
        val requestq= Volley.newRequestQueue(this)
        val stringRequest=object: StringRequest(
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
                params.put("id_st", sightid.toString())
                params.put("cate", cate.toString())
                params.put("userid", userid.toString())
                return params
            }
        }
        requestq.add(stringRequest)
    }







    private fun showimage(s:MutableList<String>){
        imagedetail.layoutManager= LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        imagedetail.adapter=AdapterCityDetail(s)
//        imagedetail.adapter.notifyDataSetChanged()

    }

    private fun gethoatdong( url:String){
        var e=Hoatdong(0,"","","","","","")
        var requestq= Volley.newRequestQueue(this)
        val jsonArrayRequest= JsonArrayRequest(
            Request.Method.GET,url,null, { response ->
                print("duoc roi")
                run {
//                    Toast.makeText(activity,"get data OK",Toast.LENGTH_LONG).show()
                        try {
                            val objects = response.getJSONObject(0)
                            e=Hoatdong(
                                    objects.getInt("id"),
                                    objects.getString("name"),
                                    objects.getString("overview"),
                                    objects.getString("location"),
                                    objects.getString("start_time"),
                                    objects.getString("end_time"),
                                    objects.getString("image")
                                )
                        } catch (error: JSONException) {
                            Log.e("error", "loi get data")
                        }

                    id=e.Id
                    name=e.Name
                    overview= e.overview
                    start_time=e.start_time
                    end_time=e.end_time
                    _location=e.location
                    namecity.text=name
                    overviewdetail.text=overview
                    location.text= "Location : $_location"
                    starttime.text="From $start_time To $end_time"
                    val urls=getString(R.string.ip) +"/travelapp/services/getimagecity.php?q=$id&c=2"
                    getImage(urls)
                    isWish()
                    getComment()

                }
            },
            { error ->
                // TODO: Handle error
                Toast.makeText(this,"Loi khi get data ", Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)

    }
}