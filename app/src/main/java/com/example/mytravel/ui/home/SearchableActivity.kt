package com.example.mytravel.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.R
import com.example.mytravel.model.City
import kotlinx.android.synthetic.main.activity_searchable.*
import kotlinx.android.synthetic.main.city_recycle_layout.view.*
import kotlinx.android.synthetic.main.recycle_search_city.view.*
import org.json.JSONException

class SearchableActivity : AppCompatActivity() {
    var full_list:MutableList<City> = mutableListOf()
    var show_list:MutableList<City> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)
        val url =getString(R.string.ip)+"/travelapp/services/getlistcity.php"
        getData(url)
//        country_list.layoutManager = LinearLayoutManager(this)
//        list_find.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
//        list_find.adapter = CityAdater(show_list)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.timkiem,menu)
        val searchItem = menu.findItem(R.id.menu_search)
        if(searchItem != null){
            val searchView = searchItem.actionView as SearchView
            searchView.queryHint="Search destination"

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {

                    show_list.clear()
                    if(newText!!.isNotEmpty()){
                        val search = newText.toLowerCase()
                        full_list.forEach {
                            if(it.name.toLowerCase().contains(search)){
                                show_list.add(it)
                            }
                        }
                    }else{
                        show_list.addAll(full_list)
                    }
//                    list_find.adapter!!.notifyDataSetChanged()
                    showcity(show_list)
                    return true
                }

            })
        }

        return super.onCreateOptionsMenu(menu)
    }


    class CityAdater( val items : MutableList<City>) : RecyclerView.Adapter<CityAdater.ViewHolder>(){


        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val i = items[position]
            holder.v.ten_city.text = i.name
            holder.v.setOnClickListener {
                println("click into image")

                val intent = Intent(holder.v.context, CityDetail::class.java)
                val bundle = Bundle()
                bundle.putInt("keyID", i.id)
                bundle.putString("nameofcity", i.name)
                bundle.putString("overview", i.overview)
                intent.putExtra("bundle", bundle)
//            intent.putExtra("keyID",citys.id)
//            intent.putExtra("nameofcity",citys.name)
                holder.v.context.startActivity(intent)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycle_search_city,parent,false))
        }

        class ViewHolder(val v: View) : RecyclerView.ViewHolder(v){
        }
    }

    private fun getData( url:String){
//        listCity.clear()
        var listCity:MutableList<City> = mutableListOf()
        var requestq= Volley.newRequestQueue(this)
        val jsonArrayRequest= JsonArrayRequest(
            Request.Method.GET,url,null, { response ->
                print("duoc roi")
                run {
//                    Toast.makeText(activity,"get data OK",Toast.LENGTH_LONG).show()
                    for (i in 0..response.length()-1) {
                        try {
                            val objects = response.getJSONObject(i)
                            println(objects.getString("city"))
                            listCity.add(
                                City(
                                    objects.getInt("id"),
                                    objects.getString("city"),
                                    objects.getString("image"),
                                    objects.getString("overview")
                                )
                            )
                            println(i)
                        } catch (error: JSONException) {
                            Log.e("error", "loi get data")
                        }
                    }

                }
                    full_list.addAll(listCity)
                showcity(full_list)
            },
            { error ->
                // TODO: Handle error
                Toast.makeText(this,"Loi khi get data ", Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)

    }
    private fun showcity(city: MutableList<City>) {
//        adapterCity =AdapterCity(city)
//        recycleviewcity.adapter = adapterCity
        list_find.adapter=CityAdater(city)
//        recycleviewcity.layoutManager = LinearLayoutManager(activity)
        list_find.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        list_find.adapter?.notifyDataSetChanged()
    }
}