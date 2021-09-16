package com.example.mytravel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_signup.*

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signup.setOnClickListener {
            if (validateFullname(fullname) && validateusername(username) && validateemail(email) && validatepassword(newpassword) && validaterepeatpw(newpassword,renewpassword) ){
                signupaccount(username.text.toString().trim(),fullname.text.toString().trim(),newpassword.text.toString().trim(),email.text.toString().trim())
            }
        }
    }
    private fun validateFullname(e:EditText):Boolean{
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
    private fun validateusername(e:EditText):Boolean{
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
    private fun validateemail(e:EditText):Boolean{
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
    private fun validatepassword(e:EditText):Boolean{
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
    private fun validaterepeatpw(e1:EditText,e2:EditText):Boolean{
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
    private fun signupaccount(a1:String,a2:String,a3:String,a4:String){
        val url= getString(R.string.ip)+"/travelapp/services/signup.php"
        val requestq= Volley.newRequestQueue(this)
        val stringRequest= object :StringRequest(
            Request.Method.POST,
            url,
            {
                    response->
                if (response.contentEquals("1")) {
                    Toast.makeText(this,"Đăng kí thành công !Đăng nhập lại để tiếp tục ", Toast.LENGTH_LONG).show()
                    gotosignin()
                } else
                {
                    username.error="Username đã tồn tại "
                    Toast.makeText(this,"Đăng kí không thành công !", Toast.LENGTH_LONG).show()
                }
            },
            {
                println("loi check thong tin dang nhap ")
                println(it.message)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["un"] = a1
                params["fn"] = a2
                params["pw"] = a3
                params["e"] = a4
                return params
            }
        }
        requestq.add(stringRequest)
    }
    private fun gotosignin(){
        username.text.clear()
        fullname.text.clear()
        newpassword.text.clear()
        renewpassword.text.clear()
        email.text.clear()
        val intent=Intent(this@Signup,Login::class.java)
        startActivity(intent)
    }

}