package com.example.mytravel.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mytravel.R
import com.example.mytravel.model.User
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        fetch()
        save.setOnClickListener {
            if (validateFullname(edit_fullname) && validateemail(edit_email) && validateusername(edit_username)){
                editprofile(edit_fullname.text.toString().trim(),edit_username.text.toString().trim(),edit_email.text.toString().trim(),
                    User.id.toString())
            }
        }
        changePW.setOnClickListener {
            startActivity(Intent(this,ChangePassword::class.java))
        }
    }
    private fun fetch(){
        edit_fullname.setText(User.fullname)
        edit_username.setText(User.username)
        edit_email.setText(User.email)
    }
    private fun editprofile(a1:String,a2:String,a3:String,a4:String){
        val url=getString(R.string.ip)+"/travelapp/services/editprofile.php"
        val requestq=Volley.newRequestQueue(this)
        val stringRequest=object :StringRequest(
            Request.Method.POST,
            url,
            {
                response->
                run {
                    if (response.contentEquals("1")) {
                        Toast.makeText(this, "Update thanh cong", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@EditProfile,ProfileFragment))
                        User.fullname=a1
                        User.username=a2
                        User.email=a3
                        this.finish()
                    }
                    else {
                        Toast.makeText(this, "Loi update profile", Toast.LENGTH_SHORT).show()
                    }

                }
            },
            {
                Toast.makeText(this,"Loi update profile",Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params["un"] = a2
                params["fn"] = a1
                params["e"] = a3
                params["id"]=a4
                return params
            }
        }
        requestq.add(stringRequest)
    }
    private fun validateFullname(e: EditText):Boolean{
        val fname=e.text.toString().trim()
        if (fname.isEmpty()){
            e.error = "Không được để trống tên "
            return false
        }
        else {
            e.error=null
            return true
        }
    }
    private fun validateusername(e: EditText):Boolean{
        val noWhiteSpaces="\\A\\w{4,20}\\z"
//        val noWhiteSpaces="^[A-Za-z]\\w{5,29}$"
        val uname=e.text.toString().trim()
        if (uname.isEmpty()){
            e.error = "Không được để trống username "
            return false
        }
        else if (uname.length >15) {
            e.error="Username quá dài "
            return false
        } else if (! uname.matches(noWhiteSpaces.toRegex())){
            e.error="Không được có khoảng trắng "
            return false
        }
        else {
            e.error=null
            return true
        }
    }
    private fun validateemail(e: EditText):Boolean{
        val email=e.text.toString().trim()
        val regexemail="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email.isEmpty()){
            e.error="Không dược bỏ trống email"
            return false
        } else if (! email.matches(regexemail.toRegex())){
            e.error="Sai email"
            return false
        } else {
            e.error=null
            return true
        }
    }
}