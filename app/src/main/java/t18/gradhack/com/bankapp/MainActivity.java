package t18.gradhack.com.bankapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_USERNAME = "t18.gradhack.com.bankapp.USERNAME";
    public static final String EXTRA_PASSWORD = "t18.gradhack.com.bankapp.PASSWORD";
    private static final String TAG = MainActivity.class.getSimpleName();

    boolean mBounded = false;
    TextToSpeechService mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "MainActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start fingerprint auth service
        startService(new Intent(getBaseContext(), FingerprintAuthService.class));

        // start tts service
        startService(new Intent(getBaseContext(), TextToSpeechService.class));
        Intent mIntent = new Intent(this, TextToSpeechService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    };

    // bind tts service instance to mTTS
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Toast.makeText(MainActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            mTTS = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//            Toast.makeText(MainActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            TextToSpeechService.LocalBinder mLocalBinder = (TextToSpeechService.LocalBinder)service;
            mTTS = mLocalBinder.getService();
//            Toast.makeText(getApplicationContext() , "TTS Service Connected", Toast.LENGTH_SHORT).show();
            mTTS.speakText("Welcome to Bank App");
        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(mBounded) {
            unbindService(mConnection);
            mBounded = false;
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
