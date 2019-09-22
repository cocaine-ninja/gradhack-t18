package com.example.test1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_account.*

class AddAccount : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)
        btn_otp.setOnClickListener{

            progressBar.visibility = View.VISIBLE
            Handler().postDelayed({
                editText_name.setText("Shivam Singh")
                editText_account_no.setText("200012012085")
                progressBar.visibility = View.INVISIBLE
            }, 4000)
        }

        btn_create_pin.setOnClickListener{
            if(editText_account_no.text.toString().isEmpty()){
                Toast.makeText(this,"Empty Fields",Toast.LENGTH_SHORT).show()
            }else {
                var myIntent = Intent(this, CreatePin::class.java)
                startActivity(myIntent)
            }
        }

        btn_cancel.setOnClickListener{
            finish()
        }

    }
}
