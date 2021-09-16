package com.example.mytravel.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.R
import com.example.mytravel.model.User
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        savepw.setOnClickListener {
            if (validatepassword(newpassword) && validaterepeatpw(newpassword, renewpassword)) {
                changepassword(
                    oldpassword.text.toString().trim(),
                    renewpassword.text.toString().trim(),
                    User.id.toString()
                )
            }
        }

    }


    private fun changepassword(a1:String,a2:String,a3:String){
        val url= getString(R.string.ip)+"/travelapp/services/changepw.php"
        val requestq= Volley.newRequestQueue(this)
        val stringRequest= object : StringRequest(
            Request.Method.POST,
            url,
            {
                    response->
                if (response.contentEquals("1")) {
                    Toast.makeText(this,"Thay doi password thanh cong !", Toast.LENGTH_LONG).show()
                    //startActivity()
                    this.finish()
                } else
                {
                    oldpassword.error="Sai password "
                    Toast.makeText(this,"Thay doi password that bai !", Toast.LENGTH_LONG).show()
                }
            },
            {
                println("loi cap nhat pw")
                println(it.message)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["op"] = a1
                params["np"] = a2
                params["id"] = a3
                return params
            }
        }
        requestq.add(stringRequest)
    }
    private fun validatepassword(e: EditText):Boolean{
        val password=e.text.toString().trim()
        val regexpassword="^" +
//                "(?=.*[0-9])" +         //at least 1 digit
//                "(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$"
        if (password.isEmpty()) {
            e.error = "Không đươc để trống password"
            return false
        } else if (! password.matches(regexpassword.toRegex())){
            e.error=" at least 1 special character \n no white spaces \n at least 4 characters"
            return false
        } else {
            e.error=null
            return true
        }
    }
    private fun validaterepeatpw(e1: EditText, e2: EditText):Boolean{
        val pw1=e1.text.toString().trim()
        val pw2=e2.text.toString().trim()
        if (pw2.isEmpty()){
            e2.error="KHông được để trống "
            return false
        } else if(pw2!=pw1) {
            e2.error="Sai mật khẩu"
            return false
        } else {
            e2.error=null
            return true
        }
    }
}