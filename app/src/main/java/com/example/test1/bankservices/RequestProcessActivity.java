package com.example.test1.bankservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;

import com.example.test1.generics.FundsRequest;
import com.example.test1.res.TextToSpeechService;


public class RequestProcessActivity extends AppCompatActivity {
    Intent intent;
    Bundle extras;

    boolean mBoundedTTS = false;
    TextToSpeechService mTTS;
    public boolean isInIt = false;

    Button[] buttonArray;
    int selectedIndex;


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

        buttonArray = new Button[] {buttonAccept, buttonDecline};
        for (Button b: buttonArray) {
            b.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        selectedIndex = -1000;

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "PROCEEDING TO PAY", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), TransactionSuccessfulActivity.class);
                intent.putExtra("PAYEE_NAME", personName);
                intent.putExtra("AMOUNT", Integer.toString(amount));
                startActivity(intent);
            }
        });
        buttonDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "PAYMENT DECLINED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), TransferRequestsActivity.class);
                startActivity(intent);
            }
        });
        //        Toast.makeText(this, personName + " " + amount + " " + purpose, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop() { super.onStop(); };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mBoundedTTS) {
            mTTS.ttsCreationSuccessful = false;
            unbindService(mConnectionTTS);
            mBoundedTTS = false;
        }
    };

    // bind tts service instance to mTTS
    ServiceConnection mConnectionTTS = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundedTTS = false;
            mTTS = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundedTTS = true;
            TextToSpeechService.LocalBinder mLocalBinderTTS = (TextToSpeechService.LocalBinder)service;
            mTTS = mLocalBinderTTS.getService();
        }
    };

    // Volumes Keys Event Listeners
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(isInIt) {
            if ((event.getFlags() & KeyEvent.FLAG_CANCELED_LONG_PRESS) == 0) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    if (selectedIndex == -1000) {
                        selectedIndex = 0;
                    } else {
                        buttonArray[selectedIndex].setBackgroundColor(Color.parseColor("#ffffff"));
                        selectedIndex = (selectedIndex + 1) % 2;
                    }
                    buttonArray[selectedIndex].setBackgroundColor(Color.parseColor("#c7000f"));
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                    buttonArray[selectedIndex].callOnClick();
                    return true;
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
