package com.example.test1.bankservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;
import com.example.test1.res.TextToSpeechService;

public class RequestProcessActivity extends AppCompatActivity {
    final Handler handler = new Handler();

    Intent intent;
    Bundle extras;

    boolean mBoundedTTS = false;
    TextToSpeechService mTTS;
    public boolean isInIt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankservices_request_process);

        intent = getIntent();
        extras = intent.getExtras();

        // bind TTS service
         Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
         bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        // delay key up event listeners
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isInIt = true;
            }
        }, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String personName = extras.getString("PERSON_NAME");
        int amount = extras.getInt("AMOUNT");
        String purpose = extras.getString("PURPOSE");

        if (isInIt) {
            mTTS.speakText("Do you want to pay rupees " + Integer.toString(amount) + " to " + personName + "?");
            Toast.makeText(this, "isInIt true", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "isInIt false", Toast.LENGTH_SHORT).show();
        
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
        // tts.speak("Do you want to pay rupees " + amount + " to " + personName, TextToSpeech.QUEUE_FLUSH, null,null);
//        Toast.makeText(getApplicationContext(), Integer.toString(amount) + " " + personName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause(){
        super.onPause();
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
}
