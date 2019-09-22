package com.example.test1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.res.TextToSpeechService;
import com.example.test1.bankservices.AccountsActivity;
import com.example.test1.bankservices.FundsTransferActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity {

    boolean mBoundedTTS = false;
    TextToSpeechService mTTS;

    private int[] menuItemsArray = {R.id.button1, R.id.button2,R.id.button3,R.id.button4};
    private Button btn1, btn2, btn3, btn4;
    private Class[] classArray = {AccountsActivity.class, ChatBotActivity.class, FundsTransferActivity.class, ChatBotActivity.class};
    private int[] buttonArrayMain = {R.drawable.homeicon1,R.drawable.homeicon2,R.drawable.homeicon3,R.drawable.homeicon4};
    private int[] buttonArraySelected = {R.drawable.homeicona,R.drawable.homeiconb,R.drawable.homeiconc,R.drawable.homeicond};
    int listViewLength = menuItemsArray.length;
    int selectedIndex;
    private boolean volume_up_pressed, volume_down_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // start TTS service
        startService(new Intent(getBaseContext(), TextToSpeechService.class));

        // bind TTS service
        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);
        btn4 = findViewById(R.id.button4);
        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this, AccountsActivity.class);
                Home.this.startActivity(myIntent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this,ChatBotActivity.class);
                Home.this.startActivity(myIntent);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this, FundsTransferActivity.class);
                Home.this.startActivity(myIntent);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(Home.this,classArray[selectedIndex]);
                Home.this.startActivity(myIntent);
            }
        });

        selectedIndex = -1000;
    }

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
        }
    };

    public void setFocus(int index) {
        Button myButton = findViewById(menuItemsArray[index]);
        myButton.setBackgroundColor(Color.parseColor("#ffffff"));
        myButton.setTextColor(Color.parseColor("#c7000f"));
        Drawable top = getResources().getDrawable(buttonArraySelected[index],null);
        myButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
    }

    public void unsetFocus(int index) {
        Button myButton = (Button)findViewById(menuItemsArray[index]);
        myButton.setBackgroundColor(Color.parseColor("#c7000f"));
        myButton.setTextColor(Color.parseColor("#ffffff"));
        Drawable top = getResources().getDrawable(buttonArrayMain[index],null);
        myButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
    }

    // navigation functions
    private void navigateUp() {
        if (selectedIndex == -1000)
            selectedIndex = 0;
        else {
            unsetFocus(selectedIndex);
            selectedIndex = (selectedIndex + 1) % listViewLength;
        }
        setFocus(selectedIndex);
    }

    private void navigateDown() {
        if (selectedIndex == -1000) {
            selectedIndex = 0;
        } else {
            unsetFocus(selectedIndex);
            selectedIndex = (selectedIndex - 1) % listViewLength;
        }
        if (selectedIndex < 0) selectedIndex += listViewLength;
        setFocus(selectedIndex);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volume_down_pressed = false;
            navigateUp();
            mTTS.speakText(((Button) findViewById(menuItemsArray[selectedIndex])).getText().toString().replace("\n", " "));
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            volume_up_pressed = false;
            navigateDown();
            mTTS.speakText(((Button) findViewById(menuItemsArray[selectedIndex])).getText().toString().replace("\n", " "));
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        boolean response = false;
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volume_down_pressed = true;
            response = true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            volume_up_pressed = true;
            response = true;
        }

        if (volume_down_pressed && volume_up_pressed) {
            Intent myIntent = new Intent(Home.this, classArray[selectedIndex]);
            Home.this.startActivity(myIntent);
        }
        return response;
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        new Timer().schedule(
                new TimerTask(){
                    @Override
                    public void run(){
                        mTTS.speakText("You are on home menu");
                    }}, 200);
    }
}
