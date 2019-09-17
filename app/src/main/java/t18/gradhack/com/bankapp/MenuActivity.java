package t18.gradhack.com.bankapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = MenuActivity.class.getSimpleName();

    private String[] menuItemsArray = {"Transaction", "Statement","Credit Card"};
    int listViewLength = menuItemsArray.length;
    ListView listView;
    int selectedIndex;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, menuItemsArray);
        listView = findViewById(R.id.menuListView);
        listView.setAdapter(adapter);

        selectedIndex = -1;
    }

    @Override
    public void onStart() {
        super.onStart();
//        listView.getChildAt(selectedIndex).setBackgroundColor(Color.CYAN);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volume_down_pressed = false;

            if (selectedIndex != -1)
                listView.getChildAt(selectedIndex).setBackgroundColor(Color.TRANSPARENT);
            selectedIndex = (selectedIndex + 1) % listViewLength;
            listView.getChildAt(selectedIndex).setBackgroundColor(Color.CYAN);

            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            volume_up_pressed = false;

            if (selectedIndex != -1)
                listView.getChildAt(selectedIndex).setBackgroundColor(Color.TRANSPARENT);
            selectedIndex = (selectedIndex - 1) % listViewLength;
            if (selectedIndex < 0) selectedIndex += listViewLength;
            listView.getChildAt(selectedIndex).setBackgroundColor(Color.CYAN);

            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volume_down_pressed = true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            volume_up_pressed = true;
        }

        if (volume_down_pressed && volume_up_pressed) {
            // switch to selected activity here
        }
        return false;
    }
}
