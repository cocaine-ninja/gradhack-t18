package t18.gradhack.com.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;

import t18.gradhack.com.generics.GenericMenuActivity;
import t18.gradhack.com.res.TextToSpeechService;

public class MenuActivity extends GenericMenuActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Log.i("MenuActivity", "onCreate() method");

        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        menuItemsArray = new String[] {"Account", "Funds Transfer", "Credit Card", "Fixed Deposits"};
        populateListView(R.id.menuListView);
        setPackageName("t18.gradhack.com.bankservices");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MenuActivity", "onPause() method");
    };

    ServiceConnection mConnectionTTS = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundedTTS = false;
            mTTS = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundedTTS = true;
            TextToSpeechService.LocalBinder mLocalBinder = (TextToSpeechService.LocalBinder)service;
            mTTS = mLocalBinder.getService();
            mTTS.speakText("Main Menu Screen");
        }
    };
}
