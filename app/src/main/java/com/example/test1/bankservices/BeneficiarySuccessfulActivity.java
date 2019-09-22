package com.example.test1.bankservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.test1.Home;
import com.example.test1.R;

public class BeneficiarySuccessfulActivity extends AppCompatActivity {

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankservices_beneficiary_successful);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // delay key up event listeners
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        }, 2500);
    }
}
