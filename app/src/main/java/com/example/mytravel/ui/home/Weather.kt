package com.example.mytravel.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.R
import com.example.mytravel.model.WeatherInfo
import kotlinx.android.synthetic.main.activity_weather.*
import org.jetbrains.anko.doAsync

class Weather : AppCompatActivity() {
    lateinit var requestQueue: RequestQueue
    lateinit var namecity:String
    lateinit var  urls:String
    var idcity:String ="ahihi"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        requestQueue=Volley.newRequestQueue(this)
        namecity= intent.getStringExtra("city")!!
        urls="https://www.metaweather.com/api/location/search/?query=$namecity"

            getidcity()
//            getweatherinfo(idcity)
//        getidcity()
        println(idcity)
    }
    fun getidcity(){
        val jsonArrayRequest=JsonArrayRequest(Request.Method.GET,urls,null,{
            respone ->
            val obj=respone.getJSONObject(0)
            idcity=obj.getString("woeid")
            println(idcity)
            println("get id city")
            getweatherinfo(idcity)
        },
            {
                error ->
                println("loi get id city")

            }
            )
        requestQueue.add(jsonArrayRequest)
//        println(idcity)
    }
    fun getweatherinfo(id:String){
        println("idcity sau khi get la : $id")
        val urls2="https://www.metaweather.com/api/location/$id"
        println(urls2)
        val jsonObjectRequest=JsonObjectRequest(Request.Method.GET,urls2,null,{
                respone ->
//            val obj=respone.getJSONObject(0)
            val info5day=respone.getJSONArray("consolidated_weather")
            println(info5day)
            val lsweatherinfo:MutableList<WeatherInfo> = mutableListOf()
            for(i in 0..info5day.length()-1){
                val obj=info5day.getJSONObject(i)
                lsweatherinfo.add(WeatherInfo(obj.getString("applicable_date"),
                    obj.getString("weather_state_name"),
                    Math.round(obj.getDouble("min_temp")*10.0)/10.0,
                    Math.round(obj.getDouble("max_temp")*10.0)/10.0,obj.getDouble("the_temp").toInt(),
                    Math.round(obj.getDouble("wind_speed")*10.0)/10.0,obj.getInt("humidity"),obj.getString("weather_state_abbr")
                    ))
            }
            lsweatherinfo.let {
                showWeather(it)
            }
        },
            {
                    error ->
                println("loi get thong tin thoi tiet")

            }
        )
        requestQueue.add(jsonObjectRequest)
    }
    fun showWeather(ls:MutableList<WeatherInfo>){
        weather_recycle.layoutManager=LinearLayoutManager(this)
        weather_recycle.adapter=AdapterWeather(ls)

    }

}