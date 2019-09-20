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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import t18.gradhack.com.generics.FundsRequest;
import t18.gradhack.com.main.R;
import t18.gradhack.com.res.FundsRequestsAdapter;
import t18.gradhack.com.res.TextToSpeechService;

public class TransferRequestsActivity extends AppCompatActivity {
    final Handler handler = new Handler();

    List<FundsRequest> requests;
    private ListView listView;
    private int listViewLength;
    private int selectedIndex;

    // TTS Engine
    public boolean mBoundedTTS = false;
    public TextToSpeechService mTTS;
    public boolean isInIt = false;

    public boolean volume_up_pressed, volume_down_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bankservices_transfer_requests);

        Intent mIntentTTS = new Intent(this, TextToSpeechService.class);
        bindService(mIntentTTS, mConnectionTTS, BIND_AUTO_CREATE);

        populateListView(R.id.requestsView);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isInIt = true;
            }
        }, 1000);
    }

    private void populateListView(int id) {
        FundsRequest req1 = new FundsRequest(1, "Sujay", 11001678321L, 100, 8010761583L, "Lunch");
        FundsRequest req2 = new FundsRequest(2, "Aman", 33892011180L, 5000, 9654741594L, "Drinks");

        requests = new ArrayList<>();
        requests.add(req1);
        requests.add(req2);

        listView = findViewById(id);

        // get data from the table by the ListAdapter
        FundsRequestsAdapter customAdapter = new FundsRequestsAdapter(this, R.layout.custom_view_for_transfer_requests, requests);
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
            Toast.makeText(getApplicationContext(), "TTS Bind successful", Toast.LENGTH_SHORT).show();
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
                    readRequest();
                    return true;
                } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
                    volume_up_pressed = false;

                    navigateUp();
                    readRequest();
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
        Toast.makeText(this, "Selected " + ((FundsRequest) listView.getItemAtPosition(selectedIndex)).getPersonName(), Toast.LENGTH_SHORT).show();
        /*
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
        
         */
    }

    private void readRequest() {
        String text = "Request from " + ((FundsRequest) listView.getItemAtPosition(selectedIndex)).getPersonName() + " ";
        text += "to pay rupees " + Integer.toString( ( (FundsRequest) listView.getItemAtPosition(selectedIndex)).getAmount() ) + " ";
        text += "for " + ((FundsRequest) listView.getItemAtPosition(selectedIndex)).getPurpose();
        mTTS.speakText(text);
    }
}
