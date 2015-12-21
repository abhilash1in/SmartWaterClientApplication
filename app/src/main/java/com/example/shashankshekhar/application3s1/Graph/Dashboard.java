package com.example.shashankshekhar.application3s1.Graph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.shashankshekhar.application3s1.R;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // init the spinner
        Spinner dropDown1= (Spinner)findViewById(R.id.spinner1);
        Spinner dropDown2= (Spinner)findViewById(R.id.spinner2);
        String[] items= new String[]{"opt1","opt2","opt3","opt4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
                items);
        dropDown1.setAdapter(adapter);
        dropDown2.setAdapter(adapter);
    }
    public void loadStaticGraph (View view) {
        Intent graphIntent = new Intent(this, StaticGraphActivity.class);
        startActivity(graphIntent);
    }
    public void loadDynamicGraph (View view) {
        Intent graphIntent = new Intent(this, DynamicGraphActivity.class);
        startActivity(graphIntent);
    }
}
