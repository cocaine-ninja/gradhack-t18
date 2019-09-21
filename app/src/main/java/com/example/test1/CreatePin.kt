package com.example.test1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_pin.*

class CreatePin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pin)

        btn_pin.setOnClickListener{
            var pin = editText_pin.text
            val sharedPref = this?.getSharedPreferences("CREDENTIAL_STORAGE",Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("PIN",pin.toString())
                commit()
            }

            finish()
        }

        btn_pin_cancel.setOnClickListener{

            finish()
        }


    }
}

