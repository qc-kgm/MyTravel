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
import com.example.mytravel.model.Sight
import com.example.mytravel.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_sight_detail.button_wish
import kotlinx.android.synthetic.main.activity_sight_detail.comment
import kotlinx.android.synthetic.main.activity_sight_detail.imagedetail
import kotlinx.android.synthetic.main.activity_sight_detail.namecity
import kotlinx.android.synthetic.main.activity_sight_detail.overviewdetail
import kotlinx.android.synthetic.main.activity_sight_detail.textcomment
import kotlinx.android.synthetic.main.activity_sight_detail.tocomment
import kotlinx.android.synthetic.main.activity_sight_detail.opentime
import org.json.JSONException

class SightDetail : AppCompatActivity() {
    var id=0
    var wish=0
    var name=""
    var overview=""
    var opentimes=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sight_detail)

        if (intent.getBundleExtra("bundle") !=null) {
            val bundle=intent.getBundleExtra("bundle")
            id=bundle!!.getInt("keyID")
            name=bundle!!.getString("nameofsight").toString()
            overview= bundle!!.getString("overview").toString()
            opentimes=bundle.getString("opentime").toString()
            namecity.text=name
            overviewdetail.text=overview
            opentime.text=opentimes
            val urls=getString(R.string.ip) +"/travelapp/services/getimagecity.php?q=$id&c=3"
            getImage(urls)
            isWish()
            getComment()


        }
        else {
            val i=intent.getIntExtra("id",1)
            getsight(getString(R.string.ip)+"/travelapp/services/getsight.php?q=$i")
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
        val cate =3
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
        val cate =3
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
        comment.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        comment.adapter=AdapterComment(ls1,ls2)

    }

    fun pushComment(){
        val cate =3
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
        val cate =3
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
        val cate =3
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
        imagedetail.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        imagedetail.adapter=AdapterCityDetail(s)
//        imagedetail.adapter.notifyDataSetChanged()

    }
    private fun getsight( url:String){
        var e=Sight(0,"","","","")
        var requestq= Volley.newRequestQueue(this)
        val jsonArrayRequest= JsonArrayRequest(
            Request.Method.GET,url,null, { response ->
                print("duoc roi")
                run {
//                    Toast.makeText(activity,"get data OK",Toast.LENGTH_LONG).show()
                        try {
                            val objects = response.getJSONObject(0)
                            println(objects.getString("name"))
                            e=Sight(
                                    objects.getInt("id"),
                                    objects.getString("name"),
                                    objects.getString("overview"),
                                    objects.getString("opentime"),
                                    objects.getString("image")
                                )
                        } catch (error: JSONException) {
                            Log.e("error", "loi get data")
                        }
                }

                id=e.id
                name=e.name
                overview= e.overview
                opentimes=e.opentime
                namecity.text=name
                overviewdetail.text=overview
                opentime.text=opentimes
                val urls=getString(R.string.ip) +"/travelapp/services/getimagecity.php?q=$id&c=3"
                getImage(urls)
                isWish()
                getComment()

            },
            { error ->
                // TODO: Handle error
                Toast.makeText(this,"Loi khi get data ", Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)

    }
}