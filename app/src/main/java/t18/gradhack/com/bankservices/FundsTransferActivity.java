package t18.gradhack.com.bankservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;

import t18.gradhack.com.generics.GenericMenuActivity;
import t18.gradhack.com.main.R;
import t18.gradhack.com.main.TextToSpeechService;

public class FundsTransferActivity extends GenericMenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds_transfer);

        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        menuItemsArray = new String[] {"Transfer Requests", "Beneficiaries", "Scheduled Transfers", "Add Beneficiary"};
        populateListView(R.id.FundsTransferMenuList);
        setPackageName("t18.gradhack.com.bankservices");
    }

    @Override
    public void onStart() {
        super.onStart();
//        listView.getChildAt(selectedIndex).setBackgroundColor(Color.CYAN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mBoundedTTS) {
            unbindService(mConnectionTTS);
            mBoundedTTS = false;
        }
    };

    ServiceConnection mConnectionTTS = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundedTTS = false;
            mTTS = null;
//            Log.i("FundsTransferActivity", "Failed to connect TTS Service");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundedTTS = true;
            TextToSpeechService.LocalBinder mLocalBinder = (TextToSpeechService.LocalBinder)service;
            mTTS = mLocalBinder.getService();
//            Log.i("FundsTransferActivity", "TTS Service Connected");
            mTTS.speakText("Selected Funds Transfer");
        }
    };
}
