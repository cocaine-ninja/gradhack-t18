package t18.gradhack.com.bankapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private String[] menuItemsArray = {"Transaction", "Statement","Credit Card"};

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
        ListView listView = findViewById(R.id.menuListView);
        listView.setAdapter(adapter);
    }
}
