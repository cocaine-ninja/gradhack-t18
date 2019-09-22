package com.example.test1.bankservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.test1.AddAccount;
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

        menuItemsArray = new String[] {"Add Accounts", "Accounts List"};
        populateListView(R.id.accountsListView);
        setPackageName("com.example.test1.bankservices");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("AccountsActivity", "onPause() method");
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
            mTTS.speakText("Accounts Screen");
        }
    };

    @Override
    protected void selectItem() {
        String item = listView.getItemAtPosition(selectedIndex).toString();
        if (item.equals("Add Account")) {
            Intent intent = new Intent(this, AddAccount.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, item + " feature coming soon", Toast.LENGTH_SHORT).show();
        }
    }
}
