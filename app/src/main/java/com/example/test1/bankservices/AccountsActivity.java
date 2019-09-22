package com.example.test1.bankservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.test1.R;
import com.example.test1.generics.GenericMenuActivity;
import com.example.test1.res.TextToSpeechService;

public class AccountsActivity extends GenericMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankservices_accounts);

        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        menuItemsArray = new String[] {"Add Account", "Accounts List"};
        populateListView(R.id.accountsListView);
        setPackageName("com.example.test1.bankservices");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AccountsActivity", "onPause() method");
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(mBoundedTTS) {
            mBoundedTTS = false;
            isInIt = false;
        }
    };

    ServiceConnection mConnectionTTS = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbindService(mConnectionTTS);
            mBoundedTTS = false;
            mTTS = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundedTTS = true;
            TextToSpeechService.LocalBinder mLocalBinder = (TextToSpeechService.LocalBinder)service;
            mTTS = mLocalBinder.getService();
            mTTS.speakText("You are on Accounts Menu");
        }
    };
}
