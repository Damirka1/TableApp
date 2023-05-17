package com.arcelormittal.tableapptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.arcelormittal.tableapptest.services.MapListService;

public class MainActivity extends AppCompatActivity {

    // Inflater
    private LayoutInflater vi;

    // buttons
    private Button generalBtn;
    private Button mapsBtn;
    private Button settingsBtn;

    // View layout
    private ConstraintLayout viewLayout;

    // Services
    private MapListService mapListService;

    private void setTabView(View v) {
        viewLayout.removeAllViews();
        viewLayout.addView(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        generalBtn = findViewById(R.id.GeneralButton);
        mapsBtn = findViewById(R.id.MapsButton);
        settingsBtn = findViewById(R.id.SettingsButton);

        viewLayout = findViewById(R.id.TabView);

        generalBtn.setOnClickListener(view -> {
            View v = vi.inflate(R.layout.general_view, null);
            setTabView(v);
        });

        generalBtn.performClick();

        mapsBtn.setOnClickListener(view -> {
            View v = vi.inflate(R.layout.maps_list, null);

            ListView list = v.findViewById(R.id.MapList);

            mapListService = new MapListService(getApplicationContext(), list, this);

            setTabView(v);
        });

        settingsBtn.setOnClickListener(view -> {
            View v = vi.inflate(R.layout.settings_view, null);
            setTabView(v);
        });
    }
}