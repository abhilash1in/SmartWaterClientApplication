package com.example.shashankshekhar.application3s1.ListView;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shashankshekhar.application3s1.R;

public class ListViewActivity extends AppCompatActivity {
    ListView listView;
    String[] values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = (ListView) findViewById(R.id.list_view);
        values  = new String[]{"Water Leakage","Water Overflow","ABC","XYZ"};
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,  android.R.id.text1,values);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("eventName",itemValue);
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
                // Show Alert
            }
        });
    }

}
