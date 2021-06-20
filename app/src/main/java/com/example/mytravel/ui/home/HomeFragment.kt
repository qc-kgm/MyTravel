package com.example.mytravel.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.R
import com.example.mytravel.databinding.FragmentHomeBinding
import com.example.mytravel.model.City
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException

class HomeFragment : Fragment() {

//    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapterCity: AdapterCity
    private lateinit var layoutManager: LinearLayoutManager
    var isVisibleAll=0

//     var listCity: MutableList<City> = mutableListOf()
//    val urls:String=getString(R.string.ip)+"/travelapp/services/getlistcity.php"
    lateinit var urls:String

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
//        test()
        urls= getString(R.string.ip) +"/travelapp/services/getlistcity.php"
        val urltop=getString(R.string.ip)+"/travelapp/services/gettopcity.php?q=3"
        getData(urls)
        getTopData(urltop)
        recycleviewcity.visibility=View.GONE
        text2.setOnClickListener {
            println("TEXT VIEW CAN CLICK ")
            if (isVisibleAll==1) {
                isVisibleAll=0
                recycleviewcity.visibility=View.GONE
                text2.setPadding(10,6,0,150)
//                (text2.layoutParams as LinearLayout.LayoutParams).apply {
//                    marginStart=8
//                    topMargin=8.dpToPixels()
//                    marginEnd=8.dpToPixels()
//                    bottomMargin=8.dpToPixels()
//                }
            }
            else
            {
                isVisibleAll=1
                recycleviewcity.visibility=View.VISIBLE
                text2.setPadding(10,6,0,10)
//                val para= text2.layoutParams as ViewGroup.MarginLayoutParams
            }
        }
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
                listCity.let {
                    showcity(it)
                }
        },
            { error ->
                // TODO: Handle error
                Toast.makeText(activity,"Loi khi get data ",Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)

    }
    fun getTopData( url:String){
//        listCity.clear()
        var listCity:MutableList<City> = mutableListOf()
        var requestq=Volley.newRequestQueue(activity)
        val jsonArrayRequest=JsonArrayRequest(
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
                            println(" ERROR GET TOP CITY")
                        }
                    }

                }
                listCity.let {
                    showtopcity(it)
                }
            },
            { error ->
                // TODO: Handle error
                println(" ERROR GET TOP CITY")
                Toast.makeText(activity,"Loi khi get data ",Toast.LENGTH_LONG).show()
                error.printStackTrace()
            })
        requestq.add(jsonArrayRequest)

    }
    private fun showcity(city: MutableList<City>) {
//        adapterCity =AdapterCity(city)
//        recycleviewcity.adapter = adapterCity
        recycleviewcity.adapter=AdapterCity(city)
//        recycleviewcity.layoutManager = LinearLayoutManager(activity)
        recycleviewcity.layoutManager=layoutManager
        recycleviewcity.adapter?.notifyDataSetChanged()
    }
    private fun showtopcity(city: MutableList<City>) {
//        adapterCity =AdapterCity(city)
//        recycleviewcity.adapter = adapterCity
        recycleviewtopcity.adapter=AdapterCity(city)
//        recycleviewcity.layoutManager = LinearLayoutManager(activity)
        recycleviewtopcity.layoutManager=LinearLayoutManager(activity)
        recycleviewtopcity.adapter?.notifyDataSetChanged()
    }
//    fun test(){
//        println(User.id)
//        User.id=3
//        println(User.id)
//    }
}
