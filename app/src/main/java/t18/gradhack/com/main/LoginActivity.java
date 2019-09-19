package t18.gradhack.com.main;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import t18.gradhack.com.main.R;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_USERNAME = "t18.gradhack.com.bankapp.USERNAME";
    public static final String EXTRA_PASSWORD = "t18.gradhack.com.bankapp.PASSWORD";

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    boolean mBoundedTTS = false;
    TextToSpeechService mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("LoginActivity", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start fingerprint auth service
        startService(new Intent(getBaseContext(), FingerprintAuthService.class));

        // start tts service
        startService(new Intent(getBaseContext(), TextToSpeechService.class));
        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        Button SRButton = findViewById(R.id.SRButton);
        SRButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startVoiceInput();
            }
        });
    }

    // start speech recognition
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
                    TextView SROutputTextView = findViewById(R.id.SROutputTextView);
                    SROutputTextView.setText(result.get(0));
                }
                break;
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(mBoundedTTS) {
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
            mTTS.speakText("Welcome to Bank App");
        }
    };

    public void login(View view) {
        Intent intent = new Intent(this, MenuActivity.class);

        Context context = getApplicationContext();
        String username = ((EditText) findViewById(R.id.usernameTextView)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordTextView)).getText().toString();

        if (username.equals("test") && password.equals("test")) {
            intent.putExtra(EXTRA_USERNAME, username);
            startActivity(intent);
        } else {
            CharSequence text = "Username or password is incorrect.";
            int duration = Toast.LENGTH_SHORT;

            Toast.makeText(context, text, duration).show();
        }
    }
}
