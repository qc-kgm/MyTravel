package com.example.mytravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.model.User
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_signup.setOnClickListener {
            val intent=Intent(this@Login, Signup::class.java)
            startActivity(intent)
        }
        btn_login.setOnClickListener {
            if(validateLoginInfo(username_login,password)) checkLoginInfo(username_login,password)
//            onBackPressed()
//            finish()
        }
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        moveTaskToBack(true)
//    }
    fun validateLoginInfo(usn:EditText,pasw:EditText):Boolean{
        val username=usn.text.toString().trim()
        val password=pasw.text.toString().trim()
        if(username.isEmpty()){
            usn.setError("Không được để trống username")
            return false
        }
        if(password.isEmpty()){
            pasw.setError("Không được để trống password")
            return false
        }
    return true

    }
    fun checkLoginInfo(usn:EditText,pasw:EditText){

        val username=usn.text.toString().trim()
        val password=pasw.text.toString().trim()
        var boolean=false
        val url= getString(R.string.ip)+"/travelapp/services/userinfo.php?un=$username&pw=$password"
        val requestq= Volley.newRequestQueue(this)
        val jsonArrayRequest= JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            {
                    response->
                    if (response.length()==0) {
                        boolean=false
                        Toast.makeText(this,"Username hoặc mật khẩu không đúng ", Toast.LENGTH_LONG).show()
                    } else
                    {
                        boolean=true
                        val obj=response.getJSONObject(0)
                        User.id=obj.getString("id").toInt()
                        User.username=obj.getString("username")
                        User.fullname=obj.getString("fullname")
                        User.email=obj.getString("email")
                        User.password=obj.getString("password")
                        usn.text.clear()
                        pasw.text.clear()
                        loginsuccess()
                    }
            },
            {
                println("loi check thong tin dang nhap ")
                println(it.message)
            }
        )
        requestq.add(jsonArrayRequest)

    }
    fun loginsuccess(){
        val intent2=Intent(this@Login,MainActivity::class.java)
        startActivity(intent2)
        this.finish()
    }
}