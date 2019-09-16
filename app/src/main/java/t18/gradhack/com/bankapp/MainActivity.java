package t18.gradhack.com.bankapp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    FingerprintAuthService fingerprintAuthService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "MainActivity.onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(getBaseContext(), FingerprintAuthService.class));
    }

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

