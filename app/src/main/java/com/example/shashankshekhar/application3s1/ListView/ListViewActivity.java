package com.example.shashankshekhar.application3s1.ListView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.shashankshekhar.application3s1.CommonUtilities.SmartWaterConstants.*;

import com.example.shashankshekhar.application3s1.R;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

import org.osmdroid.views.overlay.compass.CompassOverlay;

public class ListViewActivity extends AppCompatActivity {
    ListView listView;
    String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = (ListView) findViewById(R.id.list_view);
        values = new String[]{WATER_LEAKAGE_EVENT_NAME, WATER_OVERFLOW_EVENT_NAME, WATER_CONTAMINATION_EVENT_NAME,
                WATER_MAINTENANCE_EVENT_NAME};
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView.getItemAtPosition(position);
                // Show Alert
                showInputAlert(itemValue);
            }
        });
    }

    private void showInputAlert( final String eventName) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Description");

//        alert.setMessage("Message");
// Create TextView
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final EditText input = (EditText) inflater.inflate(R.layout.edit_text, null);
//        EditText input = (EditText)findViewById(R.layout.edit_text);
//        final EditText input = new EditText(this);
//        input.setHint("Enter details(optional)");
//        input.setTextSize(15.0f);
        input.setBackgroundColor(0x00FFFFFF);
//        input.setPadding(35,0,0,0);
        alert.setView(input);

        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String description = input.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("eventName", eventName);
                if (description != "" && description != null) {
                    resultIntent.putExtra("Description", description);
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

}
