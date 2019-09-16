package t18.gradhack.com.bankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private String[] menuItemsArray = {"Transaction", "Statement","Credit Card"};
    int listViewLength = menuItemsArray.length;
    ListView listView;
    int position;
    private boolean volume_up_pressed, volume_down_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Log.d(TAG, "MenuActivity.onCreate()");

//        Intent intent = getIntent();
//        String username = intent.getStringExtra(MainActivity.EXTRA_USERNAME);

//        Context context = getApplicationContext();
//        Toast.makeText(context, username, Toast.LENGTH_SHORT).show();

        // TextView textView = findViewById(R.id.textView);
        // textView.setText(username);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.text_view_layout_for_menu_activity, R.id.textViewForList, menuItemsArray);
        listView = findViewById(R.id.menuListView);
        listView.setAdapter(adapter);

        position = 0;
        listView.setSelection(position);
        Toast.makeText(this, (CharSequence) listView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volume_down_pressed = false;
            position = (position + 1) % listViewLength;
            Toast.makeText(this, (CharSequence) listView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            volume_up_pressed = false;
            position = (position - 1) % listViewLength;
            if (position < 0) position += listViewLength;
            Toast.makeText(this, (CharSequence) listView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            ;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volume_down_pressed = true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            volume_up_pressed = true;
        }

        if (volume_down_pressed && volume_up_pressed) {
            Toast.makeText(this, "Select " + (CharSequence) listView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
