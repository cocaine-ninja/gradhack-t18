package t18.gradhack.com.bankservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;

import t18.gradhack.com.generics.GenericMenuActivity;
import t18.gradhack.com.main.R;
import t18.gradhack.com.res.TextToSpeechService;

public class AccountsActivity extends GenericMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankservices_accounts);

        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        menuItemsArray = new String[] {"Add Account", "Accounts List"};
        populateListView(R.id.accountsListView);
        setPackageName("t18.gradhack.com.bankservices");
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
}
