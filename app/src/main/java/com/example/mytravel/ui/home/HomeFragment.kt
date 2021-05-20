package com.example.mytravel.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.R
import com.example.mytravel.databinding.FragmentHomeBinding
import com.example.mytravel.model.City
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import java.lang.Exception

class HomeFragment : Fragment() {

//    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapterCity: AdapterCity
    private lateinit var layoutManager: LinearLayoutManager

//     var listCity: MutableList<City> = mutableListOf()
    val urls:String="http://192.168.1.7/travelapp/services/getlistcity.php"


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        layoutManager=LinearLayoutManager(activity)

//        getData(urls)
////
////
////
////
//        recycleviewcity.layoutManager=LinearLayoutManager(activity)
//        recycleviewcity.adapter=AdapterCity(listCity)
//        recycleviewcity.adapter.notifyDataSetChanged()



//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        val root: View = binding.root
//        root.findViewById(R.id.fragment_home.xml)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData(urls)
//
//
//
//
//        recycleviewcity.layoutManager=LinearLayoutManager(activity)
//        recycleviewcity.adapter=AdapterCity(listCity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun getData( url:String){
//        listCity.clear()
        var listCity:MutableList<City> = mutableListOf()
        var requestq=Volley.newRequestQueue(activity)
        val jsonArrayRequest=JsonArrayRequest(
            Request.Method.GET,url,null, { response ->
                print("duoc roi")
                run {
                    Toast.makeText(activity,"get data OK",Toast.LENGTH_LONG).show()
                    for (i in 0..response.length()) {
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
                        } catch (error: JSONException) {
                            Log.e("error", "loi get data")
                        }
                    }

                }
        },
            { error ->
                // TODO: Handle error
                Toast.makeText(activity,"Loi khi get data ",Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)
        listCity.let {
            showcity(it)
        }
    }
    private fun showcity(city: MutableList<City>) {
        adapterCity =AdapterCity(city)
//        recycleviewcity.layoutManager = LinearLayoutManager(activity)
        recycleviewcity.layoutManager=layoutManager
        recycleviewcity.adapter = adapterCity
        recycleviewcity.adapter?.notifyDataSetChanged()
    }
}
