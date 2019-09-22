package com.example.test1.bankservices;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;


public class RequestProcessActivity extends AppCompatActivity {
    Intent intent;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankservices_request_process);

        intent = getIntent();
        extras = intent.getExtras();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String personName = extras.getString("PERSON_NAME");
        int amount = extras.getInt("AMOUNT");
        String purpose = extras.getString("PURPOSE");

        TextView personNameTextView = findViewById(R.id.personNameTextView);
        TextView purposeTextView = findViewById(R.id.purposeTextView);
        TextView amountTextView = findViewById(R.id.amountTextView);

        personNameTextView.setText(personName);
        amountTextView.setText(Integer.toString(amount));
        purposeTextView.setText(purpose);

        Button buttonAccept = findViewById(R.id.buttonAccept);
        Button buttonDecline = findViewById(R.id.buttonDecline);

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "PROCEEDING TO PAY", Toast.LENGTH_SHORT).show();
            }
        });
        buttonDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "PAYMENT DECLINED", Toast.LENGTH_SHORT).show();
            }
        });
        //        Toast.makeText(this, personName + " " + amount + " " + purpose, Toast.LENGTH_SHORT).show();
    }

    //TODO: add tts service
}
