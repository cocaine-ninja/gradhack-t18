package com.example.test1.bankservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;
import com.example.test1.generics.Beneficiary;
import com.example.test1.res.DummyData;
import com.example.test1.res.MyBeneficiariesAdapter;
import com.example.test1.res.TextToSpeechService;

import java.util.List;



public class MyBeneficiariesActivity extends AppCompatActivity {
    final Handler handler = new Handler();

    List<Beneficiary> requests;
    private ListView listView;
    private int listViewLength;
    private int selectedIndex;

    // TTS Engine
    public boolean mBoundedTTS = false;
    public TextToSpeechService mTTS;
    public boolean isInIt = false;

    public boolean volume_up_pressed, volume_down_pressed;

    DummyData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankservices_my_beneficiaries);

        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        populateListView(R.id.myBeneficiariesListView);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isInIt = true;
            }
        }, 1000);
    }

    private void populateListView(int id) {
        data = new DummyData();
        data.initMyBeneficiaries();
        requests = data.getMyBeneficiaries();

        listView = findViewById(id);

        // get data from the table by the ListAdapter
        MyBeneficiariesAdapter customAdapter = new MyBeneficiariesAdapter(this, R.layout.custom_view_for_my_beneficiaries, requests);
        listView.setAdapter(customAdapter);

        listViewLength = requests.size();
        selectedIndex = -1000;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TransferRequestsActivity", "onPause() method");
    };

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
//            Toast.makeText(getApplicationContext(), "TTS Bind successful", Toast.LENGTH_SHORT).show();
//            mTTS.speakText("Transfer Requests Screen");
        }
    };

    // Volumes Keys Event Listeners
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(isInIt) {
            if ((event.getFlags() & KeyEvent.FLAG_CANCELED_LONG_PRESS) == 0) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    volume_down_pressed = false;

                    navigateDown();
                    readBeneficiary();
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                    volume_up_pressed = false;

                    navigateUp();
                    readBeneficiary();
                    return true;
                }
            }
//        return super.dispatchKeyEvent(event);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        event.startTracking();
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volume_down_pressed = true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            volume_up_pressed = true;
        }

        if (volume_down_pressed && volume_up_pressed) {
            selectItem();
        }
        return true;
    }
    // navigation functions
    private void navigateUp() {
        if (selectedIndex == -1000) {
            selectedIndex = 0;
        } else {
            listView.getChildAt(selectedIndex).setBackgroundColor(Color.TRANSPARENT);
            selectedIndex = (selectedIndex - 1) % listViewLength;
        }
        if (selectedIndex < 0) selectedIndex += listViewLength;
        listView.getChildAt(selectedIndex).setBackgroundColor(Color.CYAN);
    }

    private void navigateDown() {
        if (selectedIndex == -1000) {
            selectedIndex = 0;
        } else {
            listView.getChildAt(selectedIndex).setBackgroundColor(Color.TRANSPARENT);
            selectedIndex = (selectedIndex + 1) % listViewLength;
        }
        listView.getChildAt(selectedIndex).setBackgroundColor(Color.CYAN);
    }

    private void selectItem() {
         Toast.makeText(this, "Selected " + ((Beneficiary) listView.getItemAtPosition(selectedIndex)).getName(), Toast.LENGTH_SHORT).show();

        /*
        Intent intent = new Intent(this, RequestProcessActivity.class);
        intent.putExtra("BENEFICIARY_NAME", ((Beneficiary) listView.getItemAtPosition(selectedIndex)).getName());
        intent.putExtra("BENEFICIARY_ACCOUNT_NUMBER", ((Beneficiary) listView.getItemAtPosition(selectedIndex)).getAccountNumber());
        intent.putExtra("BENEFICIARY_BANK_NAME", ((Beneficiary) listView.getItemAtPosition(selectedIndex)).getBankName());
        startActivity(intent);
         */
    }

    // tts service to speak out the beneficiary details
    private void readBeneficiary() {
        String text = ((Beneficiary) listView.getItemAtPosition(selectedIndex)).getName() + "'s";
        text += " account in " + ((Beneficiary) listView.getItemAtPosition(selectedIndex)).getBankName();
        mTTS.speakText(text);
    }
}
