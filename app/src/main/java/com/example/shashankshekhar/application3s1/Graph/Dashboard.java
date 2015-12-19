package com.example.shashankshekhar.application3s1.Graph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.shashankshekhar.application3s1.R;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
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
