package com.example.test1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btn_signup_signup.setOnClickListener{
            val username = editText_signup_username.text.toString()
            val password = editText_signup_password.text.toString()
            val sharedPref = this?.getSharedPreferences("CREDENTIAL_STORAGE", Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("USERNAME",username)
                putString("PASSWORD", password)
                commit()
            }
            val highScore = sharedPref.getString("PIN","")
            Toast.makeText(this,highScore,Toast.LENGTH_SHORT)
            finish()

        }

        btn_signup_cancel.setOnClickListener{
            finish()
        }
    }
}
