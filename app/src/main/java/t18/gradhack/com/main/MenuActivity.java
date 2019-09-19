package t18.gradhack.com.main;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {
    private String packageName = "t18.gradhack.com.bankservices";

    private String[] menuItemsArray = {"Account", "Funds Transfer", "Credit Card", "Fixed Deposits"};
    int listViewLength = menuItemsArray.length;
    ListView listView;
    int selectedIndex;

    // TTS Engine
    boolean mBoundedTTS = false;
    TextToSpeechService mTTS;

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    private boolean volume_up_pressed, volume_down_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        Log.i("MenuActivity", "onCreate() method");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItemsArray);
        listView = findViewById(R.id.menuListView);
        listView.setAdapter(adapter);

        selectedIndex = -1000;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MenuActivity", "onPause() method");
    };

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("MenuActivity", "onStop() method");

        if(mBoundedTTS) {
            mTTS.ttsIsReady = false;
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
            mTTS.speakText("Main Menu Screen");
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if((event.getFlags() & KeyEvent.FLAG_CANCELED_LONG_PRESS) == 0) {
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

    // TODO: remove speech recognition
    // start speech recognition
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
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
        String item = listView.getItemAtPosition(selectedIndex).toString();
        String className = packageName + "." + item.replace(" ", "") + "Activity";

        // convert class name string to class
        try {
            Intent intent = new Intent(this, Class.forName(className));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, item + " feature coming soon", Toast.LENGTH_SHORT).show();
        }
    }
}
