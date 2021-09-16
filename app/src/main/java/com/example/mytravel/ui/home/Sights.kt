package com.example.mytravel.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.R
import com.example.mytravel.model.Sight
import kotlinx.android.synthetic.main.activity_sights.*
import org.json.JSONException

class Sights : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sights)
        val id=intent.getIntExtra("idcity",1)
        val url=getString(R.string.ip)+"/travelapp/services/getlistsight.php?q=$id"
        getData(url)
    }

    private fun getData( url:String){
//        listCity.clear()
        var list:MutableList<Sight> = mutableListOf()
        var requestq= Volley.newRequestQueue(this)
        val jsonArrayRequest= JsonArrayRequest(
            Request.Method.GET,url,null, { response ->
                print("duoc roi")
                run {
//                    Toast.makeText(activity,"get data OK",Toast.LENGTH_LONG).show()
                    for (i in 0..response.length()-1) {
                        try {
                            val objects = response.getJSONObject(i)
                            println(objects.getString("name"))
                            list.add(
                                Sight(
                                    objects.getInt("id"),
                                    objects.getString("name"),
                                    objects.getString("overview"),
                                    objects.getString("opentime"),
                                    objects.getString("image")
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
                Toast.makeText(this,"Loi khi get data ", Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)

    }
    private fun show(sight: MutableList<Sight>) {
//        adapterCity =AdapterCity(city)
//        recycleviewcity.adapter = adapterCity
        recycle_sight.adapter=AdapterSight(sight)
//        recycleviewcity.layoutManager = LinearLayoutManager(activity)
        recycle_sight.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recycle_sight.adapter?.notifyDataSetChanged()
    }
}