package t18.gradhack.com.bankservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import t18.gradhack.com.main.R;
import t18.gradhack.com.main.TextToSpeechService;

public class FundsTransferActivity extends AppCompatActivity {
    private String packageName = "t18.gradhack.com.bankservices";
    final Handler handler = new Handler();

    // fund transfer menu items
    private String[] menuItemsArray = {"Transfer Requests", "Beneficiaries", "Scheduled Transfers", "Add Beneficiary"};
    int listViewLength = menuItemsArray.length;
    ListView listView;
    int selectedIndex;
    boolean isInIt = false;

    // TTS Engine
    boolean mBoundedTTS = false;
    TextToSpeechService mTTS;

    private boolean volume_up_pressed, volume_down_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);

        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItemsArray);
        listView = findViewById(R.id.FundTransferMenuList);
        listView.setAdapter(adapter);

        selectedIndex = -1000;

        // delay key up event listeners
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isInIt = true;
            }
        }, 500);
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
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isInIt) {
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
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            event.startTracking();

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
        String item = listView.getItemAtPosition(selectedIndex).toString();
        String className = packageName + "." + item.replace(" ", "") + "Activity";

        // convert string to class name
        try {
            Intent intent = new Intent(this, Class.forName(className));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, item + " feature coming soon", Toast.LENGTH_SHORT).show();
        }
    }
}
