package com.example.test1.bankservices;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;

import java.util.ArrayList;
import java.util.Locale;



public class AddBeneficiaryActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;
    Button buttonConfirm;

    final Handler handler = new Handler();
    public boolean isInIt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankservices_add_beneficiary);

        // delay key up event listeners
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isInIt = true;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        buttonConfirm = findViewById(R.id.buttonConfirmBeneficiary);
        buttonConfirm.setBackgroundColor(Color.parseColor("#c7000f"));
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Adding new Beneficiary", Toast.LENGTH_SHORT).show();
//                startVoiceInput();
            }
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    // handle speech recognizer output
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    TextView SROutputTextView = findViewById(R.id.beneficiaryPhoneNumber);
                    SROutputTextView.setText(result.get(0));
                }
                break;
            }

        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isInIt) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                buttonConfirm.callOnClick();
                return true;
            } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                startVoiceInput();
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
