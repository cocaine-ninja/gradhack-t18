package com.example.test1.bankservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.Home;
import com.example.test1.R;

public class TransactionSuccessfulActivity extends AppCompatActivity {
    Intent intent;
    Bundle extras;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankservices_transaction_successful);

        intent = getIntent();
        extras = intent.getExtras();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String payeeName = extras.getString("PAYEE_NAME");
        int amount = extras.getInt("AMOUNT");

        // delay key up event listeners
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        }, 5000);
    }
}
