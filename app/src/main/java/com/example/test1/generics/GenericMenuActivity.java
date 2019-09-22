package com.example.test1.generics;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.test1.R;
import com.example.test1.res.TextToSpeechService;


public class GenericMenuActivity extends AppCompatActivity {
    final Handler handler = new Handler();

    protected String packageName;

    public String[] menuItemsArray;
    public int listViewLength;

    public ListView listView;
    public int selectedIndex;

    // TTS Engine
    public boolean mBoundedTTS = false;
    public TextToSpeechService mTTS;
    public boolean isInIt = false;

    public static final int REQ_CODE_SPEECH_INPUT = 100;

    public boolean volume_up_pressed, volume_down_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        // delay key up event listeners
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isInIt = true;
            }
        }, 1000);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("GenericMenuActivity", "onPause() method");
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
//            mTTS.speakText("Main Menu Screen");
        }
    };

    public void populateListView(int id) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_view_for_generic_menu, R.id.genericMenuItemTextView, menuItemsArray);
        listView = findViewById(id);
        listView.setAdapter(adapter);

        listViewLength = menuItemsArray.length;

        selectedIndex = -1000;
    }

    public void setPackageName(String name) {
        this.packageName = name;
    }

    // Volumes Keys Event Listeners
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(isInIt) {
            if ((event.getFlags() & KeyEvent.FLAG_CANCELED_LONG_PRESS) == 0) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                    volume_down_pressed = false;

                    navigateDown();
                    mTTS.speakText(listView.getItemAtPosition(selectedIndex).toString());
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                    volume_up_pressed = false;

                    navigateUp();
                    mTTS.speakText(listView.getItemAtPosition(selectedIndex).toString());
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

    protected void selectItem() {
        String item = listView.getItemAtPosition(selectedIndex).toString();
        String className = packageName + "." + item.replace(" ", "") + "Activity";

        // convert class name string to class
        try {
            Intent intent = new Intent(this, Class.forName(className));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, item + " feature coming soon", Toast.LENGTH_SHORT).show();
            mTTS.speakText(item + " feature coming soon");
        }
    }
}
